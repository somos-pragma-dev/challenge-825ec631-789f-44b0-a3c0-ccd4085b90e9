#!/bin/bash

set -euo pipefail

readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
readonly LOGS_DIR="${PROJECT_ROOT}/logs"
readonly REPLAY_OUTPUT_DIR="${PROJECT_ROOT}/replay-output"
readonly CONFIG_FILE="${SCRIPT_DIR}/replay-config.properties"

declare -A ORDER_LOG_PATHS
declare -A MARKET_DATA_PATHS
declare -A ORDER_IDS
declare START_TIMESTAMP=""
declare END_TIMESTAMP=""
declare VERBOSE_MODE=false
declare REPLAY_MODE="full"
declare TIMEOUT_SECONDS=300
declare CHECKPOINT_INTERVAL=100

function log_info() {
    local timestamp
    timestamp=$(date '+%Y-%m-%d %H:%M:%S.%3N')
    echo "[$timestamp] [INFO] $*"
}

function log_error() {
    local timestamp
    timestamp=$(date '+%Y-%m-%d %H:%M:%S.%3N')
    echo "[$timestamp] [ERROR] $*" >&2
}

function log_debug() {
    if [[ "$VERBOSE_MODE" == "true" ]]; then
        local timestamp
        timestamp=$(date '+%Y-%m-%d %H:%M:%S.%3N')
        echo "[$timestamp] [DEBUG] $*"
    fi
}

function display_usage() {
    cat << EOF
Uso: $(basename "$0") [OPCIONES]

Script para replay determinístico de incidentes del motor de riesgo.
Reproduce escenarios de fallos usando logs de órdenes y market data.

OPCIONES:
    -s, --start TIMESTAMP      Timestamp de inicio (ISO 8601)
    -e, --end TIMESTAMP        Timestamp de fin (ISO 8601)
    -o, --order-id ID          ID de orden específica a replayar
    -l, --order-log RUTA       Ruta al log de órdenes
    -m, --market-data RUTA     Ruta al log de market data
    -r, --replay-mode MODO     Modo: full|orders|marketdata (default: full)
    -t, --timeout SEGUNDOS     Timeout en segundos (default: 300)
    -c, --checkpoint INTERVALO Intervalo de checkpoint (default: 100)
    -v, --verbose              Modo verboso
    -h, --help                 Muestra esta ayuda

EJEMPLOS:
    # Replay completo entre timestamps
    $(basename "$0") -s "2024-01-15T09:30:00Z" -e "2024-01-15T10:00:00Z"

    # Replay de orden específica
    $(basename "$0") -o "ORD-20240115-001"

    # Replay con logs específicos
    $(basename "$0") -l "/path/to/orders.log" -m "/path/to/market.log"

EOF
}

function parse_arguments() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -s|--start)
                START_TIMESTAMP="$2"
                shift 2
                ;;
            -e|--end)
                END_TIMESTAMP="$2"
                shift 2
                ;;
            -o|--order-id)
                ORDER_IDS["${#ORDER_IDS[@]}"]="$2"
                shift 2
                ;;
            -l|--order-log)
                ORDER_LOG_PATHS["default"]="$2"
                shift 2
                ;;
            -m|--market-data)
                MARKET_DATA_PATHS["default"]="$2"
                shift 2
                ;;
            -r|--replay-mode)
                REPLAY_MODE="$2"
                shift 2
                ;;
            -t|--timeout)
                TIMEOUT_SECONDS="$2"
                shift 2
                ;;
            -c|--checkpoint)
                CHECKPOINT_INTERVAL="$2"
                shift 2
                ;;
            -v|--verbose)
                VERBOSE_MODE=true
                shift
                ;;
            -h|--help)
                display_usage
                exit 0
                ;;
            *)
                log_error "Opción desconocida: $1"
                display_usage
                exit 1
                ;;
        esac
    done

    if [[ -z "${START_TIMESTAMP}" ]] && [[ -z "${END_TIMESTAMP}" ]] && [[ ${#ORDER_IDS[@]} -eq 0 ]]; then
        log_error "Debe especificar --start/--end o --order-id"
        display_usage
        exit 1
    fi
}

function initialize_environment() {
    log_info "Inicializando entorno de replay..."

    if [[ ! -d "$LOGS_DIR" ]]; then
        log_error "Directorio de logs no encontrado: $LOGS_DIR"
        exit 1
    fi

    mkdir -p "$REPLAY_OUTPUT_DIR"

    if [[ ${#ORDER_LOG_PATHS[@]} -eq 0 ]]; then
        local order_log_file
        order_log_file=$(find "$LOGS_DIR" -name "orders*.log" -type f 2>/dev/null | head -n1)
        if [[ -n "$order_log_file" ]]; then
            ORDER_LOG_PATHS["default"]="$order_log_file"
            log_info "Log de órdenes encontrado: $order_log_file"
        else
            log_error "No se encontró ningún log de órdenes en $LOGS_DIR"
            exit 1
        fi
    fi

    if [[ ${#MARKET_DATA_PATHS[@]} -eq 0 ]]; then
        local market_data_file
        market_data_file=$(find "$LOGS_DIR" -name "marketdata*.log" -type f 2>/dev/null | head -n1)
        if [[ -n "$market_data_file" ]]; then
            MARKET_DATA_PATHS["default"]="$market_data_file"
            log_info "Log de market data encontrado: $market_data_file"
        fi
    fi

    log_info "Entorno inicializado correctamente"
}

function validate_timestamp_format() {
    local timestamp="$1"
    if [[ ! "$timestamp" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\.[0-9]+)?(Z|[+-][0-9]{2}:[0-9]{2})?$ ]]; then
        log_error "Formato de timestamp inválido: $timestamp"
        return 1
    fi
    return 0
}

function timestamp_to_epoch() {
    local timestamp="$1"
    date -d "$timestamp" '+%s%3N' 2>/dev/null || date -j -f "%Y-%m-%dT%H:%M:%S%z" "$timestamp" '+%s%3N' 2>/dev/null
}

function filter_orders_by_timerange() {
    local order_log="$1"
    local output_file="$2"
    local start_epoch end_epoch

    if [[ -n "$START_TIMESTAMP" ]]; then
        start_epoch=$(timestamp_to_epoch "$START_TIMESTAMP")
    else
        start_epoch=0
    fi

    if [[ -n "$END_TIMESTAMP" ]]; then
        end_epoch=$(timestamp_to_epoch "$END_TIMESTAMP")
    else
        end_epoch=$(date '+%s%3N')
    fi

    log_info "Filtrando órdenes entre $start_epoch y $end_epoch"

    awk -F'|' -v start="$start_epoch" -v end="$end_epoch" '
    BEGIN { OFS="|" }
    {
        timestamp = $1
        if (timestamp >= start && timestamp <= end) {
            print $0
        }
    }' "$order_log" > "$output_file"

    local count
    count=$(wc -l < "$output_file")
    log_info "Órdenes filtradas: $count"
}

function filter_orders_by_id() {
    local order_log="$1"
    local output_file="$2"
    local order_id

    for order_id in "${ORDER_IDS[@]}"; do
        log_info "Buscando orden: $order_id"
        grep -F "$order_id" "$order_log" >> "$output_file" || true
    done

    local count
    count=$(wc -l < "$output_file")
    log_info "Órdenes encontradas: $count"
}

function extract_market_data_for_orders() {
    local market_log="$1"
    local orders_file="$2"
    local output_file="$3"

    log_info "Extrayendo market data para las órdenes..."

    local instruments
    instruments=$(cut -d'|' -f5 "$orders_file" 2>/dev/null | sort -u)

    > "$output_file"
    local instrument
    while IFS= read -r instrument; do
        if [[ -n "$instrument" ]]; then
            log_debug "Filtrando market data para instrumento: $instrument"
            grep -F "$instrument" "$market_log" >> "$output_file" || true
        fi
    done <<< "$instruments"

    local count
    count=$(wc -l < "$output_file")
    log_info "Market data extraída: $count eventos"
}

function generate_checkpoint() {
    local checkpoint_number="$1"
    local checkpoint_dir="${REPLAY_OUTPUT_DIR}/checkpoint-${checkpoint_number}"

    mkdir -p "$checkpoint_dir"

    local order_log_path
    for order_log_path in "${ORDER_LOG_PATHS[@]}"; do
        if [[ -f "$order_log_path" ]]; then
            cp "$order_log_path" "$checkpoint_dir/"
        fi
    done

    log_debug "Checkpoint $checkpoint_number creado en $checkpoint_dir"
}

function replay_order() {
    local order_data="$1"
    local market_data_file="$2"

    local order_id timestamp trader_id strategy_id instrument side quantity price
    order_id=$(echo "$order_data" | cut -d'|' -f2)
    timestamp=$(echo "$order_data" | cut -d'|' -f1)
    trader_id=$(echo "$order_data" | cut -d'|' -f3)
    strategy_id=$(echo "$order_data" | cut -d'|' -f4)
    instrument=$(echo "$order_data" | cut -d'|' -f5)
    side=$(echo "$order_data" | cut -d'|' -f6)
    quantity=$(echo "$order_data" | cut -d'|' -f7)
    price=$(echo "$order_data" | cut -d'|' -f8)

    log_info "Replaying orden: $order_id"
    log_debug "  Timestamp: $timestamp"
    log_debug "  Trader: $trader_id, Estrategia: $strategy_id"
    log_debug "  Instrumento: $instrument, Lado: $side, Cantidad: $quantity, Precio: $price"

    local relevant_market_data
    relevant_market_data=$(grep -F "$instrument" "$market_data_file" | head -n100)

    local market_data_count
    market_data_count=$(echo "$relevant_market_data" | wc -l)
    log_debug "  Market data relevante: $market_data_count eventos"

    echo "$order_id|$timestamp|$trader_id|$strategy_id|$instrument|$side|$quantity|$price|REPLAYED" >> "${REPLAY_OUTPUT_DIR}/replayed-orders.log"
}

function execute_replay() {
    local filtered_orders="${REPLAY_OUTPUT_DIR}/filtered-orders.log"
    local filtered_marketdata="${REPLAY_OUTPUT_DIR}/filtered-marketdata.log"
    local replay_start_time
    replay_start_time=$(date +%s)

    log_info "Iniciando replay en modo: $REPLAY_MODE"

    case "$REPLAY_MODE" in
        full)
            if [[ ${#ORDER_LOG_PATHS[@]} -gt 0 ]]; then
                local order_log="${ORDER_LOG_PATHS[default]}"
                if [[ ${#ORDER_IDS[@]} -gt 0 ]]; then
                    filter_orders_by_id "$order_log" "$filtered_orders"
                else
                    filter_orders_by_timerange "$order_log" "$filtered_orders"
                fi
            fi

            if [[ ${#MARKET_DATA_PATHS[@]} -gt 0 ]]; then
                local market_log="${MARKET_DATA_PATHS[default]}"
                if [[ -f "$filtered_orders" ]]; then
                    extract_market_data_for_orders "$market_log" "$filtered_orders" "$filtered_marketdata"
                else
                    cp "$market_log" "$filtered_marketdata"
                fi
            fi
            ;;
        orders)
            if [[ ${#ORDER_LOG_PATHS[@]} -gt 0 ]]; then
                local order_log="${ORDER_LOG_PATHS[default]}"
                if [[ ${#ORDER_IDS[@]} -gt 0 ]]; then
                    filter_orders_by_id "$order_log" "$filtered_orders"
                else
                    filter_orders_by_timerange "$order_log" "$filtered_orders"
                fi
            fi
            > "$filtered_marketdata"
            ;;
        marketdata)
            > "$filtered_orders"
            if [[ ${#MARKET_DATA_PATHS[@]} -gt 0 ]]; then
                local market_log="${MARKET_DATA_PATHS[default]}"
                cp "$market_log" "$filtered_marketdata"
            fi
            ;;
        *)
            log_error "Modo de replay desconocido: $REPLAY_MODE"
            exit 1
            ;;
    esac

    if [[ ! -f "$filtered_orders" ]] || [[ $(wc -l < "$filtered_orders") -eq 0 ]]; then
        log_error "No hay órdenes para replay"
        exit 1
    fi

    local order_count=0
    local checkpoint_count=0

    while IFS= read -r order_line; do
        if [[ -n "$order_line" ]]; then
            replay_order "$order_line" "$filtered_marketdata"
            order_count=$((order_count + 1))

            if [[ $((order_count % CHECKPOINT_INTERVAL)) -eq 0 ]]; then
                checkpoint_count=$((checkpoint_count + 1))
                generate_checkpoint "$checkpoint_count"

                local current_time
                current_time=$(date +%s)
                local elapsed=$((current_time - replay_start_time))
                log_info "Progreso: $order_count órdenes procesadas, elapsed: ${elapsed}s"

                if [[ $elapsed -gt $TIMEOUT_SECONDS ]]; then
                    log_error "Timeout alcanzado después de $elapsed segundos"
                    exit 1
                fi
            fi
        fi
    done < "$filtered_orders"

    local replay_end_time
    replay_end_time=$(date +%s)
    local total_time=$((replay_end_time - replay_start_time))

    log_info "Replay completado: $order_count órdenes en ${total_time}s"
    log_info "Resultados guardados en: $REPLAY_OUTPUT_DIR"
}

function generate_replay_report() {
    local report_file="${REPLAY_OUTPUT_DIR}/replay-report.txt"

    log_info "Generando reporte de replay..."

    {
        echo "========================================"
        echo "  REPLAY DETERMINÍSTICO - REPORTE"
        echo "========================================"
        echo ""
        echo "Fecha de ejecución: $(date '+%Y-%m-%d %H:%M:%S')"
        echo ""
        echo "Parámetros:"
        echo "  Modo de replay: $REPLAY_MODE"
        echo "  Timestamp inicio: ${START_TIMESTAMP:-no especificado}"
        echo "  Timestamp fin: ${END_TIMESTAMP:-no especificado}"
        echo "  Órdenes objetivo: ${ORDER_IDS[*]:-todas en rango}"
        echo "  Timeout: ${TIMEOUT_SECONDS}s"
        echo "  Intervalo de checkpoint: $CHECKPOINT_INTERVAL"
        echo ""
        echo "Fuentes de datos:"
        for key in "${!ORDER_LOG_PATHS[@]}"; do
            echo "  Order log [$key]: ${ORDER_LOG_PATHS[$key]}"
        done
        for key in "${!MARKET_DATA_PATHS[@]}"; do
            echo "  Market data [$key]: ${MARKET_DATA_PATHS[$key]}"
        done
        echo ""
        echo "Resultados:"

        if [[ -f "${REPLAY_OUTPUT_DIR}/replayed-orders.log" ]]; then
            local replayed_count
            replayed_count=$(wc -l < "${REPLAY_OUTPUT_DIR}/replayed-orders.log")
            echo "  Órdenes replayeradas: $replayed_count"
        fi

        if [[ -f "${REPLAY_OUTPUT_DIR}/filtered-orders.log" ]]; then
            local filtered_count
            filtered_count=$(wc -l < "${REPLAY_OUTPUT_DIR}/filtered-orders.log")
            echo "  Órdenes filtradas: $filtered_count"
        fi

        echo ""
        echo "========================================"
    } > "$report_file"

    log_info "Reporte generado: $report_file"
}

function main() {
    log_info "=== INICIO REPLAY DETERMINÍSTICO ==="
    log_info "Directorio del proyecto: $PROJECT_ROOT"
    log_info "Directorio de salida: $REPLAY_OUTPUT_DIR"

    parse_arguments "$@"

    if [[ -n "$START_TIMESTAMP" ]]; then
        validate_timestamp_format "$START_TIMESTAMP" || exit 1
    fi

    if [[ -n "$END_TIMESTAMP" ]]; then
        validate_timestamp_format "$END_TIMESTAMP" || exit 1
    fi

    initialize_environment
    execute_replay
    generate_replay_report

    log_info "=== REPLAY DETERMINÍSTICO COMPLETADO ==="
}

main "$@"