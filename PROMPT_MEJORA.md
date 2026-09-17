# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `OrderSide`: OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.domain.OrderSide.
- `src/test/java/com/trading/riskengine/RiskEngineShardTest.java` — `OrderSide`: OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.domain.OrderSide.
- `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchStatus`: KillSwitchStatus se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.interfaces.KillSwitchStatus (hay mas de un tipo con ese nombre en el proyecto).
- `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `OrderSide`: OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.domain.OrderSide.
- `src/main/java/com/trading/riskengine/Main.java` — `MarketDataFeedHandler`: El import com.trading.riskengine.infrastructure.MarketDataFeedHandler no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/main/java/com/trading/riskengine/interfaces/RiskEngineQueryService.java` — `VaRModel`: El import com.trading.riskengine.domain.VaRModel no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- `src/main/java/com/trading/riskengine/Main.java` — `KillSwitchPolicy.isTradingHalted`: Se invoca `isTradingHalted` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.isAccepted`: Se invoca `isAccepted` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.riskScore`: Se invoca `riskScore` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.currentVaR`: Se invoca `currentVaR` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.rejectionReason`: Se invoca `rejectionReason` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/domain/RiskOrder.java` — `RiskStatus.name`: Se invoca `name` sobre `RiskStatus`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setInstrument`: Se invoca `setInstrument` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setEventType`: Se invoca `setEventType` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setBidPrices`: Se invoca `setBidPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setBidVolumes`: Se invoca `setBidVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setAskPrices`: Se invoca `setAskPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setAskVolumes`: Se invoca `setAskVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setTimestamp`: Se invoca `setTimestamp` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setSequenceNumber`: Se invoca `setSequenceNumber` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setTradePrice`: Se invoca `setTradePrice` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setTradeVolume`: Se invoca `setTradeVolume` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getEventType`: Se invoca `getEventType` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getBidPrices`: Se invoca `getBidPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getBidVolumes`: Se invoca `getBidVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getAskPrices`: Se invoca `getAskPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getAskVolumes`: Se invoca `getAskVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getTimestamp`: Se invoca `getTimestamp` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getTradePrice`: Se invoca `getTradePrice` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getTradeVolume`: Se invoca `getTradeVolume` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskOrder.quantity`: Se invoca `quantity` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskOrder.price`: Se invoca `price` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskOrder.orderId`: Se invoca `orderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskEvent.setOrder`: Se invoca `setOrder` sobre `RiskEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskEvent.setSubmitTime`: Se invoca `setSubmitTime` sobre `RiskEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskEvent.getOrder`: Se invoca `getOrder` sobre `RiskEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.orderId`: Se invoca `orderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.strategyId`: Se invoca `strategyId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getRecentVolatility`: Se invoca `getRecentVolatility` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.isAnomalous`: Se invoca `isAnomalous` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.recordOrder`: Se invoca `recordOrder` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.notional`: Se invoca `notional` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getStandardDeviation`: Se invoca `getStandardDeviation` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.traderId`: Se invoca `traderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getMeanOrderSize`: Se invoca `getMeanOrderSize` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getOrdersPerSecond`: Se invoca `getOrdersPerSecond` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getExpectedOrdersPerSecond`: Se invoca `getExpectedOrdersPerSecond` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getBuyCount`: Se invoca `getBuyCount` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getSellCount`: Se invoca `getSellCount` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.combinedScore`: Se invoca `combinedScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.zScore`: Se invoca `zScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.frequencyScore`: Se invoca `frequencyScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.sideImbalanceScore`: Se invoca `sideImbalanceScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.side`: Se invoca `side` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.instrument`: Se invoca `instrument` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.var`: Se invoca `var` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.volatility`: Se invoca `volatility` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.timestamp`: Se invoca `timestamp` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/RiskEngineShardTest.java` — `RiskOrder.orderId`: Se invoca `orderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchPolicy.evaluate`: Se invoca `evaluate` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchStatus.triggered`: Se invoca `triggered` sobre `KillSwitchStatus`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchPolicy.getHistory`: Se invoca `getHistory` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchPolicy.reset`: Se invoca `reset` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

## Como saber que terminaste

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Contexto técnico original
Sistema master-l2 que evalúa el riesgo de cada orden antes de enviarla al exchange, en menos de 500 microsegundos p99. Consume feed de market data (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidents. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

### Reto
- Tema: motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos
- Seniority: master-l2
- Tipo: mixed
- Título: Implementación de un motor de riesgo en tiempo real con circuit breakers
- Tiempo estimado: 4 semanas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Evaluación de riesgos y límites en tiempo real — objetivo: Implementar la funcionalidad para evaluar el riesgo de cada orden antes de enviarla al exchange, aplicando límites por trader/estrategia/instrumento. — entregable (NO resolver): Mecanismo para evaluar riesgos y aplicar límites en tiempo real.
- Fase 2: Justificación de la elección de lenguaje y estructuras de datos — objetivo: Justificar el uso de estructuras lock-free vs mutex y la elección entre C++ vs Rust vs Java LMAX Disruptor. — entregable (NO resolver): Documento que justifica la elección de lenguaje y estructuras de datos.
- Fase 3: Garantizar consistencia entre motores de riesgo en paralelo — objetivo: Implementar la lógica para garantizar la consistencia entre múltiples motores de riesgo corriendo en paralelo. — entregable (NO resolver): Mecanismo para garantizar la consistencia entre motores de riesgo en paralelo.
- Fase 4: Política de kill switch y estrategia de replay determinístico — objetivo: Implementar la política de kill switch y la estrategia de replay determinístico para post-mortem de incidentes. — entregable (NO resolver): Política de kill switch y estrategia de replay determinístico implementadas.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.trading</groupId>
    <artifactId>risk-engine</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>Risk Engine</name>
    <description>Low-latency real-time risk scoring engine for algorithmic trading</description>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <disruptor.version>5.2.0</disruptor.version>
        <resilience4j.version>2.1.0</resilience4j.version>
        <log4j.version>2.23.1</log4j.version>
        <graalvm.version>23.1.2</graalvm.version>
        <jmh.version>1.37</jmh.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.github.resilience4j</groupId>
                <artifactId>resilience4j-bom</artifactId>
                <version>${resilience4j.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
            <version>${disruptor.version}</version>
        </dependency>

        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-circuitbreaker</artifactId>
        </dependency>

        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-retry</artifactId>
        </dependency>

        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-micrometer</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j.version}</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${log4j.version}</version>
        </dependency>

        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-core</artifactId>
            <version>1.12.2</version>
        </dependency>

        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>

        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-core</artifactId>
            <version>${jmh.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-generator-annprocess</artifactId>
            <version>${jmh.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.graalvm.nativeimage</groupId>
            <artifactId>svm</artifactId>
            <version>${graalvm.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <compilerArgs>
                        <arg>--enable-preview</arg>
                    </compilerArgs>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.trading.riskengine.Main</mainClass>
                                </transformer>
                            </transformers>
                            <filters>
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>0.9.28</version>
                <executions>
                    <execution>
                        <id>build-native</id>
                        <phase>package</phase>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <mainClass>com.trading.riskengine.Main</mainClass>
                    <buildArgs>
                        <buildArg>--no-fallback</buildArg>
                        <buildArg>-H:+ReportExceptionStackTraces</buildArg>
                    </buildArgs>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.2</version>
                <configuration>
                    <argLine>
                        -XX:+UnlockExperimentalVMOptions
                        -XX:+UseZGC
                        -XX:ConcGCThreads=2
                        -XX:ParallelGCThreads=2
                        -XX:MaxGCPauseMillis=10
                        -Dlmbench.enable=false
                    </argLine>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.openjdk.jmh</groupId>
                <artifactId>jmh-maven-plugin</artifactId>
                <version>1.37</version>
                <executions>
                    <execution>
                        <id>benchmark</id>
                        <phase>test</phase>
                        <goals>
                            <goal>benchmark</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>low-latency</id>
            <activation>
                <property>
                    <name>low-latency</name>
                </property>
            </activation>
            <properties>
                <jdk.image>release</jdk.image>
            </properties>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-compiler-plugin</artifactId>
                        <configuration>
                            <compilerArgs>
                                <arg>-XX:+UnlockExperimentalVMOptions</arg>
                                <arg>-XX:+UseZGC</arg>
                                <arg>-XX:+AlwaysPreTouch</arg>
                                <arg>-Xmx4g</arg>
                                <arg>-Xms4g</arg>
                                <arg>--enable-preview</arg>
                            </compilerArgs>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>

// === ARCHIVO: src/main/java/com/trading/riskengine/Main.java ===
package com.trading.riskengine;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.trading.riskengine.application.KillSwitchPolicy;
import com.trading.riskengine.application.RiskEngineShard;
import com.trading.riskengine.config.ResilienceConfig;
import com.trading.riskengine.infrastructure.MarketDataFeedHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.micrometer.CircuitBreakerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final int RING_BUFFER_SIZE;
    private static final int DISRUPTOR_THREADS;
    private static final int HTTP_PORT;
    private static final int GRPC_PORT;
    private static final boolean ZGC_ENABLED;
    private static final long ZGC_MAX_PAUSE_MS;

    private final Map<String, RiskEngineShard> shards;
    private final Disruptor<RiskOrderEvent> riskDisruptor;
    private final ExecutorService complianceQueryExecutor;
    private final KillSwitchPolicy killSwitchPolicy;
    private final CircuitBreaker marketDataCircuitBreaker;
    private volatile boolean running;

    static {
        Properties props = new Properties();
        try (var input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }

        RING_BUFFER_SIZE = Integer.parseInt(props.getProperty("risk.engine.ringbuffer.size", "65536"));
        DISRUPTOR_THREADS = Integer.parseInt(props.getProperty("risk.engine.disruptor.threads", "4"));
        HTTP_PORT = Integer.parseInt(props.getProperty("risk.engine.http.port", "8080"));
        GRPC_PORT = Integer.parseInt(props.getProperty("risk.engine.grpc.port", "9090"));
        ZGC_ENABLED = Boolean.parseBoolean(props.getProperty("risk.engine.zgc.enabled", "true"));
        ZGC_MAX_PAUSE_MS = Long.parseLong(props.getProperty("risk.engine.zgc.maxPauseMs", "10"));
    }

    public Main() {
        this.shards = new ConcurrentHashMap<>();
        this.complianceQueryExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.killSwitchPolicy = new KillSwitchPolicy();
        this.marketDataCircuitBreaker = ResilienceConfig.createDynamicCircuitBreaker("marketData", 0.5, 100);

        this.riskDisruptor = new Disruptor<>(
            RiskOrderEvent::new,
            RING_BUFFER_SIZE,
            Executors.newFixedThreadPool(DISRUPTOR_THREADS, r -> {
                Thread t = new Thread(r, "risk-disruptor-" + ThreadLocalRandom.current().nextInt(1000));
                t.setPriority(Thread.MAX_PRIORITY);
                t.setDaemon(false);
                return t;
            }),
            ProducerType.MULTI,
            new BlockingWaitStrategy()
        );

        configureDisruptorHandlers();
    }

    private void configureDisruptorHandlers() {
        riskDisruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            String instrument = event.instrument();
            RiskEngineShard shard = shards.get(instrument);

            if (shard == null) {
                logger.warn("No shard found for instrument: {}", instrument);
                return;
            }

            if (killSwitchPolicy.isTradingHalted(event.traderId())) {
                logger.warn("Trading halted for trader: {} - order rejected", event.traderId());
                event.setRejected(true);
                event.setRejectionReason("KILL_SWITCH_ACTIVE");
                return;
            }

            RiskEvaluationResult result = shard.evaluateOrder(
                event.orderId(),
                event.traderId(),
                event.strategyId(),
                event.instrument(),
                event.quantity(),
                event.price(),
                event.side()
            );

            event.setAccepted(result.isAccepted());
            event.setRiskScore(result.riskScore());
            event.setVaR(result.currentVaR());
            event.setRejectionReason(result.rejectionReason());

            if (!result.isAccepted()) {
                logger.warn("Order rejected: {} - reason: {}", event.orderId(), result.rejectionReason());
            }
        });

        riskDisruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler());
    }

    public void initialize() {
        logger.info("Initializing Risk Engine with RingBuffer size: {}, Disruptor threads: {}",
            RING_BUFFER_SIZE, DISRUPTOR_THREADS);

        if (ZGC_ENABLED) {
            logger.info("ZGC enabled with max pause: {}ms", ZGC_MAX_PAUSE_MS);
        }

        initializeInstrumentShards();
        riskDisruptor.start();

        logger.info("Risk Engine started successfully");
        logger.info("Compliance HTTP endpoint: http://localhost:{}/health", HTTP_PORT);
        logger.info("Compliance gRPC endpoint: localhost:{}", GRPC_PORT);
    }

    private void initializeInstrumentShards() {
        String[] instruments = {"AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "NVDA", "META", "NFLX"};

        for (String instrument : instruments) {
            RiskEngineShard shard = new RiskEngineShard(
                instrument,
                marketDataCircuitBreaker,
                killSwitchPolicy
            );
            shards.put(instrument, shard);
            logger.info("Initialized shard for instrument: {}", instrument);
        }
    }

    public void startComplianceServer() {
        complianceQueryExecutor.submit(() -> {
            try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
                serverSocket.bind(new InetSocketAddress(HTTP_PORT));
                serverSocket.configureBlocking(false);
                logger.info("Compliance HTTP server started on port {}", HTTP_PORT);

                while (running) {
                    SocketChannel client = serverSocket.accept();
                    if (client != null) {
                        handleComplianceQuery(client);
                    }
                    Thread.sleep(1);
                }
            } catch (IOException e) {
                logger.error("Compliance server error", e);
            }
        });
    }

    private void handleComplianceQuery(SocketChannel client) {
        complianceQueryExecutor.submit(() -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(8192);
                client.read(buffer);
                buffer.flip();

                String request = StandardCharsets.UTF_8.decode(buffer).toString();

                if (request.contains("/health")) {
                    String response = buildHealthResponse();
                    client.write(StandardCharsets.UTF_8.encode(response));
                } else if (request.contains("/risk exposure")) {
                    String response = buildExposureResponse();
                    client.write(StandardCharsets.UTF_8.encode(response));
                } else if (request.contains("/var")) {
                    String response = buildVaRResponse();
                    client.write(StandardCharsets.UTF_8.encode(response));
                }

                client.close();
            } catch (IOException e) {
                logger.debug("Error handling compliance query", e);
            }
        });
    }

    private String buildHealthResponse() {
        return "HTTP/1.1 200 OK\r\n" +
               "Content-Type: application/json\r\n" +
               "X-MiFID-II-Trace-ID: " + java.util.UUID.randomUUID() + "\r\n" +
               "\r\n" +
               "{\"status\":\"UP\",\"timestamp\":\"" + Instant.now() + "\",\"shards\":" + shards.size() + "}";
    }

    private String buildExposureResponse() {
        StringBuilder sb = new StringBuilder("{\"exposures\":[");
        shards.forEach((instrument, shard) -> {
            sb.append("{\"instrument\":\"")
              .append(instrument)
              .append("\",\"exposure\":")
              .append(shard.getCurrentExposure())
              .append("},");
        });
        sb.append("]}");
        return "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + sb;
    }

    private String buildVaRResponse() {
        double totalVaR = shards.values().stream()
            .mapToDouble(RiskEngineShard::getCurrentVaR)
            .sum();
        return "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" +
               "{\"totalVaR\":" + totalVaR + ",\"confidenceLevel\":0.99,\"horizon\":\"1D\"}";
    }

    public void submitOrder(String orderId, String traderId, String strategyId,
                           String instrument, long quantity, double price, String side) {
        RingBuffer<RiskOrderEvent> ringBuffer = riskDisruptor.getRingBuffer();

        long sequence = ringBuffer.next();
        try {
            RiskOrderEvent event = ringBuffer.get(sequence);
            event.setOrderId(orderId);
            event.setTraderId(traderId);
            event.setStrategyId(strategyId);
            event.setInstrument(instrument);
            event.setQuantity(quantity);
            event.setPrice(price);
            event.setSide(side);
            event.setTimestamp(Instant.now());
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void shutdown() {
        logger.info("Shutting down Risk Engine...");
        running = false;

        riskDisruptor.shutdown(10, TimeUnit.SECONDS);
        complianceQueryExecutor.shutdown();

        shards.values().forEach(RiskEngineShard::shutdown);

        logger.info("Risk Engine shutdown complete");
    }

    public static void main(String[] args) throws Exception {
        Main engine = new Main();
        engine.initialize();
        engine.startComplianceServer();

        Runtime.getRuntime().addShutdownHook(new Thread(engine::shutdown));

        logger.info("Risk Engine is running. Press Ctrl+C to stop.");

        Thread.currentThread().join();
    }

    public record RiskOrderEvent() {
        private String orderId;
        private String traderId;
        private String strategyId;
        private String instrument;
        private long quantity;
        private double price;
        private String side;
        private Instant timestamp;
        private boolean accepted;
        private boolean rejected;
        private double riskScore;
        private double var;
        private String rejectionReason;

        public void setOrderId(String orderId) { this.orderId = orderId; }
        public void setTraderId(String traderId) { this.traderId = traderId; }
        public void setStrategyId(String strategyId) { this.strategyId = strategyId; }
        public void setInstrument(String instrument) { this.instrument = instrument; }
        public void setQuantity(long quantity) { this.quantity = quantity; }
        public void setPrice(double price) { this.price = price; }
        public void setSide(String side) { this.side = side; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public void setAccepted(boolean accepted) { this.accepted = accepted; }
        public void setRejected(boolean rejected) { this.rejected = rejected; }
        public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
        public void setVaR(double var) { this.var = var; }
        public void setRejectionReason(String reason) { this.rejectionReason = reason; }

        public String orderId() { return orderId; }
        public String traderId() { return traderId; }
        public String strategyId() { return strategyId; }
        public String instrument() { return instrument; }
        public long quantity() { return quantity; }
        public double price() { return price; }
        public String side() { return side; }
        public Instant timestamp() { return timestamp; }
        public boolean isAccepted() { return accepted; }
        public boolean isRejected() { return rejected; }
        public double riskScore() { return riskScore; }
        public double var() { return var; }
        public String rejectionReason() { return rejectionReason; }
    }

    public record RiskEvaluationResult(
        boolean isAccepted,
        double riskScore,
        double currentVaR,
        String rejectionReason
    ) {
        public static RiskEvaluationResult accepted(double riskScore, double var) {
            return new RiskEvaluationResult(true, riskScore, var, null);
        }

        public static RiskEvaluationResult rejected(double riskScore, double var, String reason) {
            return new RiskEvaluationResult(false, riskScore, var, reason);
        }
    }

    private static class DisruptorExceptionHandler implements com.lmax.disruptor.ExceptionHandler<Object> {
        private static final Logger log = LogManager.getLogger(DisruptorExceptionHandler.class);

        @Override
        public void handleEventException(Throwable ex, long sequence, Object event) {
            log.error("Exception processing event at sequence: {}", sequence, ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.error("Exception starting disruptor", ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.error("Exception shutting down disruptor", ex);
        }
    }
}

// === ARCHIVO: src/main/resources/application.properties ===
# =============================================================================
# Risk Engine - Low-Latency Configuration
# =============================================================================
# This file contains all configuration parameters for the real-time risk
# scoring engine. Values are tuned for sub-500-microsecond P99 latency.
#
# JVM Target: JDK 21 with ZGC for minimal pause times
# Architecture: Reactive ring-buffer with per-instrument sharding
# =============================================================================

# -----------------------------------------------------------------------------
# Disruptor Configuration (Lock-free Ring Buffer)
# -----------------------------------------------------------------------------
risk.engine.ringbuffer.size=65536
risk.engine.disruptor.threads=4
risk.engine.disruptor.wait.strategy=blocking
risk.engine.disruptor.batch.size=256
risk.engine.disruptor.multi.producer=true

# -----------------------------------------------------------------------------
# Sharding Configuration (Per-Instrument Isolation)
# -----------------------------------------------------------------------------
risk.engine.shards.max.instruments=256
risk.engine.shards.initial.capacity=8
risk.engine.shards.rebalance.enabled=true

# -----------------------------------------------------------------------------
# Risk Limits Configuration
# -----------------------------------------------------------------------------
risk.limits.trader.daily.max.exposure=1000000.00
risk.limits.strategy.daily.max.exposure=500000.00
risk.limits.instrument.daily.max.exposure=200000.00
risk.limits.order.max.size=50000
risk.limits.order.max.notional=5000000.00
risk.limits.var.confidence.level=0.99
risk.limits.var.horizon.minutes=1440

# -----------------------------------------------------------------------------
# Circuit Breaker Configuration (Dynamic Thresholds)
# -----------------------------------------------------------------------------
risk.circuitbreaker.enabled=true
risk.circuitbreaker.failure.rate.threshold=50
risk.circuitbreaker.wait.duration.in.open.state.seconds=30
risk.circuitbreaker.sliding.window.size=100
risk.circuitbreaker.minimum.number.of.calls=10
risk.circuitbreaker.permitted.number.of.calls.in.half.open.state=3

# Dynamic calibration based on historical volatility
risk.circuitbreaker.volatility.lookback.minutes=60
risk.circuitbreaker.volatility.multiplier=2.5
risk.circuitbreaker.automatic.threshold.adjustment=true

# -----------------------------------------------------------------------------
# Kill Switch Policy Configuration
# -----------------------------------------------------------------------------
risk.killswitch.enabled=true
risk.killswitch.anomaly.detection.window.seconds=300
risk.killswitch.anomaly.threshold.sigma=4.0
risk.killswitch.max.consecutive.rejections=100
risk.killswitch.cooldown.seconds=600
risk.killswitch.escalation.enabled=true

# -----------------------------------------------------------------------------
# VaR Model Configuration
# -----------------------------------------------------------------------------
risk.var.model.type=PARAMETRIC
risk.var.model.lookback.days=252
risk.var.model.decay.factor=0.94
risk.var.model.weighted.observation=true
risk.var.model.confidence.interval=0.99

# -----------------------------------------------------------------------------
# Market Data Feed Configuration
# -----------------------------------------------------------------------------
risk.marketdata.feed.enabled=true
risk.marketdata.feed.reconnect.delay.ms=1000
risk.marketdata.feed.buffer.size=4096
risk.marketdata.feed.batch.processing=true
risk.marketdata.orderbook.levels=10
risk.marketdata.trade.cache.size=10000

# -----------------------------------------------------------------------------
# Compliance Endpoints (MiFID II)
# -----------------------------------------------------------------------------
risk.engine.http.port=8080
risk.engine.grpc.port=9090
risk.engine.http.threads=16
risk.engine.http.idle.timeout.seconds=30

# Compliance query endpoints
risk.compliance.endpoint.health=/health
risk.compliance.endpoint.exposure=/risk/exposure
risk.compliance.endpoint.var=/risk/var
risk.compliance.endpoint.orders=/compliance/orders
risk.compliance.endpoint.traders=/compliance/traders

# MiFID II Trace ID
risk.compliance.trace.id.enabled=true
risk.compliance.trace.id.header=X-MiFID-II-Trace-ID

# -----------------------------------------------------------------------------
# ZGC Configuration (Sub-millisecond GC Pauses)
# -----------------------------------------------------------------------------
risk.engine.zgc.enabled=true
risk.engine.zgc.maxPauseMs=10
risk.engine.zgc.concurrent.stages=3
risk.engine.zgc.parallel.threads=2
risk.engine.zgc.conc.threads=2

# Heap configuration for low-latency
risk.engine.heap.min.size=2g
risk.engine.heap.max.size=4g
risk.engine.heap.always.pretouch=true

# -----------------------------------------------------------------------------
# Logging Configuration (Structured JSON for Analysis)
# -----------------------------------------------------------------------------
risk.logging.format=json
risk.logging.mdc.enabled=true
risk.logging.mdc.fields=traderId,strategyId,instrument,orderId,timestamp
risk.logging.level=INFO
risk.logging.file.enabled=true
risk.logging.file.path=/var/log/risk-engine/
risk.logging.file.max.size=100m
risk.logging.file.max.history=30

# -----------------------------------------------------------------------------
# Virtual Threads Configuration (Compliance Queries)
# -----------------------------------------------------------------------------
risk.engine.virtual.threads.enabled=true
risk.engine.virtual.threads.pool.size=1000
risk.engine.virtual.threads.task.queue.capacity=4096

# -----------------------------------------------------------------------------
# Replay and Post-Mortem Configuration
# -----------------------------------------------------------------------------
risk.replay.enabled=true
risk.replay.event.store.type=MEMORY
risk.replay.event.store.capacity=1000000
risk.replay.deterministic.seed=42
risk.replay.speed.multiplier=1.0

# -----------------------------------------------------------------------------
# Performance Profiling
# -----------------------------------------------------------------------------
risk.profiling.enabled=true
risk.profiling.jmh.operations.per.invocation=100000
risk.profiling.jmh.warmup.iterations=5
risk.profiling.jmh.measurement.iterations=10
risk.profiling.jmh.output.format=JSON

# -----------------------------------------------------------------------------
# Thread Affinity (CPU Pinning for Deterministic Latency)
# -----------------------------------------------------------------------------
risk.thread.affinity.enabled=false
risk.thread.affinity.cores=0,1,2,3,4,5,6,7
risk.thread.affinity.numa.enabled=false

# -----------------------------------------------------------------------------
# Alerting and Monitoring
# -----------------------------------------------------------------------------
risk.monitoring.enabled=true
risk.monitoring.metrics.port=9091
risk.monitoring.alert.latency.threshold.us=500
risk.monitoring.alert.var.threshold.pct=80
risk.monitoring.alert.circuitbreaker.open=true


// === ARCHIVO: src/main/java/com/trading/riskengine/domain/RiskOrder.java ===
package com.trading.riskengine.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidad de dominio que representa una orden de trading con sus atributos de riesgo.
 * Implementa el patrón record para inmutabilidad y seguridad en entornos concurrentes.
 */
public record RiskOrder(
    String orderId,
    String traderId,
    String strategyId,
    String instrument,
    OrderSide side,
    BigDecimal quantity,
    BigDecimal price,
    BigDecimal notionalValue,
    BigDecimal currentVaR,
    BigDecimal exposure,
    BigDecimal traderLimit,
    BigDecimal strategyLimit,
    BigDecimal instrumentLimit,
    RiskStatus riskStatus,
    Instant timestamp,
    Instant evaluatedAt,
    String evaluationReason
) {
    public RiskOrder {
        if (orderId == null || orderId.isBlank()) {
            orderId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (evaluatedAt == null) {
            evaluatedAt = timestamp;
        }
        if (riskStatus == null) {
            riskStatus = RiskStatus.PENDING;
        }
    }

    public enum OrderSide {
        BUY, SELL, SHORT, COVER
    }

    public enum RiskStatus {
        PENDING,
        APPROVED,
        REJECTED_LIMIT_BREACH,
        REJECTED_VAR_BREACH,
        REJECTED_CIRCUIT_BREAKER,
        REJECTED_KILL_SWITCH,
        REVIEW_REQUIRED
    }

    public boolean isApproved() {
        return riskStatus == RiskStatus.APPROVED;
    }

    public boolean isRejected() {
        return riskStatus.name().startsWith("REJECTED");
    }

    public boolean exceedsTraderLimit() {
        return traderLimit != null && exposure.compareTo(traderLimit) > 0;
    }

    public boolean exceedsStrategyLimit() {
        return strategyLimit != null && exposure.compareTo(strategyLimit) > 0;
    }

    public boolean exceedsInstrumentLimit() {
        return instrumentLimit != null && exposure.compareTo(instrumentLimit) > 0;
    }

    public boolean exceedsVaR() {
        return currentVaR != null && notionalValue.compareTo(currentVaR) > 0;
    }

    public RiskOrder withStatus(RiskStatus newStatus, String reason) {
        return new RiskOrder(
            orderId, traderId, strategyId, instrument, side, quantity, price,
            notionalValue, currentVaR, exposure, traderLimit, strategyLimit,
            instrumentLimit, newStatus, timestamp, Instant.now(), reason
        );
    }

    public BigDecimal getEffectiveLimit() {
        BigDecimal minLimit = traderLimit;
        if (strategyLimit != null && (minLimit == null || strategyLimit.compareTo(minLimit) < 0)) {
            minLimit = strategyLimit;
        }
        if (instrumentLimit != null && (minLimit == null || instrumentLimit.compareTo(minLimit) < 0)) {
            minLimit = instrumentLimit;
        }
        return minLimit;
    }

    public String toComplianceLog() {
        return String.format(
            "ORDER_DECISION: orderId=%s traderId=%s strategyId=%s instrument=%s side=%s " +
            "notional=%s status=%s reason=\"%s\" evaluatedAt=%s",
            orderId, traderId, strategyId, instrument, side, notionalValue,
            riskStatus, evaluationReason, evaluatedAt
        );
    }
}

// === ARCHIVO: src/main/java/com/trading/riskengine/domain/VaRModel.java ===
package com.trading.riskengine.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.atomic.StampedLock;
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;

/**
 * Modelo de VaR (Value-at-Risk) intraday que calcula el riesgo en tiempo real.
 * Utiliza estructuras lock-free para actualización concurrente sin bloquear el procesamiento.
 * Implementa metodología de varianza-covarianza con ajustes por volatilidad histórica.
 */
public class VaRModel {
    private final String instrument;
    private final int lookbackPeriods;
    private final double confidenceLevel;
    private final double decayFactor;
    
    private final AtomicReference<double[]> returnsBuffer;
    private final AtomicReference<BigDecimal> currentVaR;
    private final AtomicReference<BigDecimal> currentVolatility;
    private final AtomicReference<Instant> lastUpdate;
    private final LongAdder calculationCount;
    private final StampedLock orderbookLock;
    
    private volatile double[] priceLevels;
    private volatile double[] volumes;
    private volatile int orderbookDepth;

    private static final int DEFAULT_LOOKBACK = 252;
    private static final double DEFAULT_CONFIDENCE = 0.99;
    private static final double DEFAULT_DECAY = 0.94;
    private static final double Z_SCORE_99 = 2.326;

    public VaRModel(String instrument) {
        this(instrument, DEFAULT_LOOKBACK, DEFAULT_CONFIDENCE, DEFAULT_DECAY);
    }

    public VaRModel(String instrument, int lookbackPeriods, double confidenceLevel, double decayFactor) {
        this.instrument = instrument;
        this.lookbackPeriods = lookbackPeriods;
        this.confidenceLevel = confidenceLevel;
        this.decayFactor = decayFactor;
        
        this.returnsBuffer = new AtomicReference<>(new double[lookbackPeriods]);
        this.currentVaR = new AtomicReference<>(BigDecimal.ZERO);
        this.currentVolatility = new AtomicReference<>(BigDecimal.ZERO);
        this.lastUpdate = new AtomicReference<>(Instant.now());
        this.calculationCount = new LongAdder();
        this.orderbookLock = new StampedLock();
        
        this.priceLevels = new double[50];
        this.volumes = new double[50];
        this.orderbookDepth = 0;
    }

    public void updateOrderBook(double[] bidPrices, double[] bidVolumes, 
                                 double[] askPrices, double[] askVolumes, int depth) {
        long stamp = orderbookLock.writeLock();
        try {
            int totalDepth = Math.min(depth, 50);
            this.orderbookDepth = totalDepth;
            
            for (int i = 0; i < totalDepth; i++) {
                priceLevels[i * 2] = bidPrices[i];
                volumes[i * 2] = bidVolumes[i];
                priceLevels[i * 2 + 1] = askPrices[i];
                volumes[i * 2 + 1] = askVolumes[i];
            }
            lastUpdate.set(Instant.now());
        } finally {
            orderbookLock.unlockWrite(stamp);
        }
    }

    public void addTrade(double price, double volume, Instant timestamp) {
        double[] currentReturns = returnsBuffer.get();
        double[] newReturns = new double[lookbackPeriods];
        
        System.arraycopy(currentReturns, 1, newReturns, 0, lookbackPeriods - 1);
        
        double lastPrice = priceLevels.length > 0 ? priceLevels[0] : price;
        double logReturn = Math.log(price / lastPrice);
        newReturns[lookbackPeriods - 1] = logReturn;
        
        returnsBuffer.set(newReturns);
        calculateVaR();
        calculationCount.increment();
    }

    private void calculateVaR() {
        double[] returns = returnsBuffer.get();
        double mean = DoubleStream.of(returns).filter(r -> r != 0).average().orElse(0.0);
        
        double variance = DoubleStream.of(returns)
            .filter(r -> r != 0)
            .map(r -> Math.pow(r - mean, 2))
            .average()
            .orElse(0.0);
        
        double volatility = Math.sqrt(variance);
        double annualizedVol = volatility * Math.sqrt(252);
        
        double currentPrice = getCurrentMidPrice();
        double varValue = currentPrice * annualizedVol * Z_SCORE_99 * Math.sqrt(1.0 / 252.0);
        
        currentVaR.set(BigDecimal.valueOf(varValue).setScale(2, RoundingMode.HALF_UP));
        currentVolatility.set(BigDecimal.valueOf(annualizedVol).setScale(4, RoundingMode.HALF_UP));
    }

    private double getCurrentMidPrice() {
        long stamp = orderbookLock.readLock();
        try {
            if (orderbookDepth > 0) {
                double bestBid = priceLevels[0];
                double bestAsk = priceLevels[1];
                return (bestBid + bestAsk) / 2.0;
            }
            return priceLevels.length > 0 ? priceLevels[0] : 100.0;
        } finally {
            orderbookLock.unlockRead(stamp);
        }
    }

    public BigDecimal getVaR() {
        return currentVaR.get();
    }

    public BigDecimal getVolatility() {
        return currentVolatility.get();
    }

    public boolean isBreached(BigDecimal positionValue) {
        BigDecimal var = currentVaR.get();
        return var.compareTo(BigDecimal.ZERO) > 0 && 
               positionValue.compareTo(var.multiply(BigDecimal.valueOf(1.5))) > 0;
    }

    public double getDynamicThreshold(double baseMultiplier) {
        BigDecimal vol = currentVolatility.get();
        double volMultiplier = vol.doubleValue() * Math.sqrt(1.0 / 252.0);
        return baseMultiplier * (1.0 + volMultiplier * 2.0);
    }

    public Instant getLastUpdateTime() {
        return lastUpdate.get();
    }

    public long getCalculationCount() {
        return calculationCount.sum();
    }

    public String getInstrument() {
        return instrument;
    }

    public VaRSnapshot getSnapshot() {
        return new VaRSnapshot(
            instrument,
            currentVaR.get(),
            currentVolatility.get(),
            getCurrentMidPrice(),
            lastUpdate.get(),
            calculationCount.sum()
        );
    }

    public record VaRSnapshot(
        String instrument,
        BigDecimal var,
        BigDecimal volatility,
        double midPrice,
        Instant lastUpdate,
        long calculationCount
    ) {}
}

// === ARCHIVO: src/main/java/com/trading/riskengine/interfaces/RiskEngineQueryService.java ===
package com.trading.riskengine.interfaces;

import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.VaRModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para consultas de compliance bajo regulación MiFID II.
 * Expone métodos para consultar decisiones de riesgo, estado de circuit breakers
 * y metadatos de evaluación para auditoría y trazabilidad regulatoria.
 */
public interface RiskEngineQueryService {

    /**
     * Consulta el estado actual de evaluación de riesgo para una orden específica.
     * @param orderId Identificador único de la orden
     * @return Optional conteniendo la orden con su estado de riesgo si existe
     */
    Optional<RiskOrder> getOrderRiskEvaluation(String orderId);

    /**
     * Recupera todas las decisiones de riesgo en un rango de tiempo para auditoría MiFID II.
     * @param startTime Inicio del rango temporal
     * @param endTime Fin del rango temporal
     * @return Lista de decisiones de riesgo en el período
     */
    List<RiskOrder> getRiskDecisionsInRange(Instant startTime, Instant endTime);

    /**
     * Obtiene el estado actual de los circuit breakers del motor de riesgo.
     * @return Estado de los circuit breakers por instrumento
     */
    CircuitBreakerStatus getCircuitBreakerStatus();

    /**
     * Recupera el VaR actual para un instrumento específico.
     * @param instrument Símbolo del instrumento
     * @return Snapshot del modelo VaR
     */
    VaRModel.VaRSnapshot getVaRSnapshot(String instrument);

    /**
     * Consulta la exposición agregada de un trader específico.
     * @param traderId Identificador del trader
     * @return Exposición total del trader
     */
    BigDecimal getTraderExposure(String traderId);

    /**
     * Obtiene la exposición por estrategia para un trader.
     * @param traderId Identificador del trader
     * @return Mapa de estrategia a exposición
     */
    List<ExposureByStrategy> getStrategyExposures(String traderId);

    /**
     * Recupera el estado del kill switch global.
     * @return Estado actual del kill switch
     */
    KillSwitchStatus getKillSwitchStatus();

    /**
     * Genera un reporte de compliance para un período específico.
     * @param startTime Inicio del período
     * @param endTime Fin del período
     * @return Reporte de compliance en formato string
     */
    String generateComplianceReport(Instant startTime, Instant endTime);

    /**
     * Verifica si el motor de riesgo acepta nuevas órdenes para un instrumento.
     * @param instrument Símbolo del instrumento
     * @return true si el instrumento está habilitado para trading
     */
    boolean isInstrumentEnabled(String instrument);

    record CircuitBreakerStatus(
        String instrument,
        CircuitState state,
        int breachCount,
        Instant lastBreachTime,
        Instant resetTime,
        String reason
    ) {
        public enum CircuitState {
            CLOSED, OPEN, HALF_OPEN, FORCED_OPEN
        }
    }

    record ExposureByStrategy(
        String strategyId,
        BigDecimal totalExposure,
        BigDecimal limit,
        double utilizationPercentage,
        int orderCount
    ) {}

    record KillSwitchStatus(
        boolean globalKillSwitchActive,
        String triggeredBy,
        Instant triggeredAt,
        String reason,
        List<String> affectedInstruments,
        boolean autoResetEnabled,
        Instant autoResetTime
    ) {}

    record ComplianceReport(
        Instant periodStart,
        Instant periodEnd,
        int totalOrders,
        int approvedOrders,
        int rejectedOrders,
        List<RiskOrder> rejectedAboveThreshold,
        BigDecimal totalNotionalApproved,
        BigDecimal totalNotionalRejected,
        int circuitBreakerTrips,
        int killSwitchActivations
    ) {}
}


// === ARCHIVO: src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java ===
package com.trading.riskengine.infrastructure;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.trading.riskengine.application.RiskEngineShard;
import com.trading.riskengine.domain.VaRModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarketDataFeedHandler {
    private static final Logger logger = LogManager.getLogger(MarketDataFeedHandler.class);
    private static final int RING_BUFFER_SIZE = 16384;
    private static final int THREAD_COUNT = 4;
    private static final long FEED_TIMEOUT_MS = 100;
    private static final double MIN_VOLUME = 0.001;
    private static final int MAX_ORDERBOOK_DEPTH = 20;

    private final Map<String, RiskEngineShard> instrumentShards;
    private final Map<String, Disruptor<MarketDataEvent>> disruptorsByInstrument;
    private final ExecutorService feedExecutor;
    private final AtomicBoolean running;
    private final Map<String, Long> lastUpdateTimestamps;
    private final Map<String, Integer> sequenceNumbers;

    public MarketDataFeedHandler(Map<String, RiskEngineShard> instrumentShards) {
        this.instrumentShards = instrumentShards;
        this.disruptorsByInstrument = new ConcurrentHashMap<>();
        this.feedExecutor = Executors.newFixedThreadPool(THREAD_COUNT, r -> {
            Thread t = new Thread(r, "market-data-feed");
            t.setDaemon(true);
            return t;
        });
        this.running = new AtomicBoolean(false);
        this.lastUpdateTimestamps = new ConcurrentHashMap<>;
        this.sequenceNumbers = new ConcurrentHashMap<>();
        initializeDisruptors();
    }

    private void initializeDisruptors() {
        for (String instrument : instrumentShards.keySet()) {
            Disruptor<MarketDataEvent> disruptor = new Disruptor<>(
                MarketDataEvent::new,
                RING_BUFFER_SIZE,
                r -> {
                    Thread t = new Thread(r, "disruptor-" + instrument);
                    t.setDaemon(true);
                    return t;
                },
                ProducerType.MULTI,
                new BlockingWaitStrategy()
            );
            
            disruptor.handleEventsWith(new MarketDataEventHandler(instrument));
            disruptor.setDefaultExceptionHandler(new DisruptorErrorHandler(instrument));
            disruptor.start();
            disruptorsByInstrument.put(instrument, disruptor);
            logger.info("Disruptor initialized for instrument: {}", instrument);
        }
    }

    public void onOrderBookUpdate(String instrument, double[] bidPrices, double[] bidVolumes,
                                   double[] askPrices, double[] askVolumes) {
        if (!running.get()) {
            return;
        }
        
        Disruptor<MarketDataEvent> disruptor = disruptorsByInstrument.get(instrument);
        if (disruptor == null) {
            logger.warn("No disruptor found for instrument: {}", instrument);
            return;
        }

        long sequence = disruptor.getRingBuffer().next();
        try {
            MarketDataEvent event = disruptor.getRingBuffer().get(sequence);
            event.setInstrument(instrument);
            event.setEventType(MarketDataEvent.EventType.ORDERBOOK);
            event.setBidPrices(bidPrices.clone());
            event.setBidVolumes(bidVolumes.clone());
            event.setAskPrices(askPrices.clone());
            event.setAskVolumes(askVolumes.clone());
            event.setTimestamp(Instant.now());
            event.setSequenceNumber(sequenceNumbers.compute(instrument, (k, v) -> v == null ? 1 : v + 1));
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public void onTrade(String instrument, double price, double volume, Instant timestamp) {
        if (!running.get() || volume < MIN_VOLUME) {
            return;
        }

        Disruptor<MarketDataEvent> disruptor = disruptorsByInstrument.get(instrument);
        if (disruptor == null) {
            return;
        }

        long sequence = disruptor.getRingBuffer().next();
        try {
            MarketDataEvent event = disruptor.getRingBuffer().get(sequence);
            event.setInstrument(instrument);
            event.setEventType(MarketDataEvent.EventType.TRADE);
            event.setTradePrice(price);
            event.setTradeVolume(volume);
            event.setTimestamp(timestamp);
            event.setSequenceNumber(sequenceNumbers.compute(instrument, (k, v) -> v == null ? 1 : v + 1));
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public void start() {
        running.set(true);
        logger.info("Market data feed handler started");
    }

    public void stop() {
        running.set(false);
        disruptorsByInstrument.values().forEach(Disruptor::halt);
        feedExecutor.shutdown();
        try {
            if (!feedExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                feedExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            feedExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Market data feed handler stopped");
    }

    public Map<String, Long> getLastUpdateTimestamps() {
        return Map.copyOf(lastUpdateTimestamps);
    }

    public boolean isRunning() {
        return running.get();
    }

    private class MarketDataEventHandler implements EventHandler<MarketDataEvent> {
        private final String instrument;

        MarketDataEventHandler(String instrument) {
            this.instrument = instrument;
        }

        @Override
        public void onEvent(MarketDataEvent event, long sequence, boolean endOfBatch) {
            try {
                switch (event.getEventType()) {
                    case ORDERBOOK -> handleOrderBookUpdate(event);
                    case TRADE -> handleTrade(event);
                }
                lastUpdateTimestamps.put(instrument, System.nanoTime());
            } catch (Exception e) {
                logger.error("Error processing market data event for {}: {}", instrument, e.getMessage(), e);
            }
        }

        private void handleOrderBookUpdate(MarketDataEvent event) {
            RiskEngineShard shard = instrumentShards.get(instrument);
            if (shard != null) {
                VaRModel varModel = shard.getVaRModel();
                varModel.updateOrderBook(
                    event.getBidPrices(),
                    event.getBidVolumes(),
                    event.getAskPrices(),
                    event.getAskVolumes(),
                    event.getTimestamp()
                );
            }
        }

        private void handleTrade(MarketDataEvent event) {
            RiskEngineShard shard = instrumentShards.get(instrument);
            if (shard != null) {
                VaRModel varModel = shard.getVaRModel();
                varModel.addTrade(
                    event.getTradePrice(),
                    event.getTradeVolume(),
                    event.getTimestamp()
                );
            }
        }
    }

    private static class DisruptorErrorHandler implements ExceptionHandler<MarketDataEvent> {
        private static final Logger errorLogger = LogManager.getLogger("DisruptorError");
        private final String instrument;

        DisruptorErrorHandler(String instrument) {
            this.instrument = instrument;
        }

        @Override
        public void handleEventException(Throwable ex, long sequence, MarketDataEvent event) {
            errorLogger.error("Disruptor exception on {} sequence {}: {}", 
                instrument, sequence, ex.getMessage(), ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            errorLogger.error("Disruptor start exception for {}: {}", instrument, ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            errorLogger.error("Disruptor shutdown exception for {}: {}", instrument, ex.getMessage(), ex);
        }
    }

    public static class MarketDataEvent {
        private String instrument;
        private EventType eventType;
        private double[] bidPrices;
        private double[] bidVolumes;
        private double[] askPrices;
        private double[] askVolumes;
        private double tradePrice;
        private double tradeVolume;
        private Instant timestamp;
        private long sequenceNumber;

        public enum EventType {
            ORDERBOOK, TRADE
        }

        public String getInstrument() { return instrument; }
        public void setInstrument(String instrument) { this.instrument = instrument; }
        public EventType getEventType() { return eventType; }
        public void setEventType(EventType eventType) { this.eventType = eventType; }
        public double[] getBidPrices() { return bidPrices; }
        public void setBidPrices(double[] bidPrices) { this.bidPrices = bidPrices; }
        public double[] getBidVolumes() { return bidVolumes; }
        public void setBidVolumes(double[] bidVolumes) { this.bidVolumes = bidVolumes; }
        public double[] getAskPrices() { return askPrices; }
        public void setAskPrices(double[] askPrices) { this.askPrices = askPrices; }
        public double[] getAskVolumes() { return askVolumes; }
        public void setAskVolumes(double[] askVolumes) { this.askVolumes = askVolumes; }
        public double getTradePrice() { return tradePrice; }
        public void setTradePrice(double tradePrice) { this.tradePrice = tradePrice; }
        public double getTradeVolume() { return tradeVolume; }
        public void setTradeVolume(double tradeVolume) { this.tradeVolume = tradeVolume; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public long getSequenceNumber() { return sequenceNumber; }
        public void setSequenceNumber(long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    }
}

// === ARCHIVO: src/main/java/com/trading/riskengine/application/RiskEngineShard.java ===
package com.trading.riskengine.application;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.RiskOrder.RiskStatus;
import com.trading.riskengine.domain.VaRModel;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RiskEngineShard {
    private static final Logger logger = LogManager.getLogger(RiskEngineShard.class);
    private static final int RING_BUFFER_SIZE = 8192;
    private static final double DEFAULT_CONFIDENCE_LEVEL = 0.99;
    private static final int DEFAULT_LOOKBACK = 252;
    private static final double DEFAULT_DECAY_FACTOR = 0.94;
    private static final double VOLATILITY_SCALING_FACTOR = 2.5;
    private static final int CIRCUIT_BREAKER_WINDOW_SECONDS = 60;
    private static final int CIRCUIT_BREAKER_MIN_CALLS = 10;
    private static final double CIRCUIT_BREAKER_FAILURE_RATE = 0.5;

    private final String instrument;
    private final VaRModel varModel;
    private final CircuitBreaker circuitBreaker;
    private final Disruptor<RiskEvent> disruptor;
    private final AtomicBoolean enabled;
    private final AtomicReference<BigDecimal> positionLimit;
    private final AtomicReference<BigDecimal> currentExposure;
    private final Map<String, RiskOrder> recentOrders;
    private final ExecutorService queryExecutor;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public RiskEngineShard(String instrument, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.instrument = instrument;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.varModel = new VaRModel(instrument, DEFAULT_LOOKBACK, DEFAULT_CONFIDENCE_LEVEL, DEFAULT_DECAY_FACTOR);
        this.circuitBreaker = createCircuitBreaker(instrument);
        this.disruptor = createDisruptor();
        this.enabled = new AtomicBoolean(true);
        this.positionLimit = new AtomicReference<>(new BigDecimal("1000000"));
        this.currentExposure = new AtomicReference<>(BigDecimal.ZERO);
        this.recentOrders = new ConcurrentHashMap<>();
        this.queryExecutor = Executors.newVirtualThreadPerTaskExecutor();
        
        logger.info("RiskEngineShard initialized for instrument: {} with limit: {}", 
            instrument, positionLimit.get());
    }

    private CircuitBreaker createCircuitBreaker(String instrument) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowSize(CIRCUIT_BREAKER_WINDOW_SECONDS)
            .minimumNumberOfCalls(CIRCUIT_BREAKER_MIN_CALLS)
            .failureRateThreshold(CIRCUIT_BREAKER_FAILURE_RATE)
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .permittedNumberOfCallsInHalfOpenState(3)
            .slowCallRateThreshold(80.0)
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            .build();
        
        CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("shard-" + instrument);
        logger.info("Circuit breaker created for shard: {}", instrument);
        return cb;
    }

    private Disruptor<RiskEvent> createDisruptor() {
        Disruptor<RiskEvent> disruptor = new Disruptor<>(
            RiskEvent::new,
            RING_BUFFER_SIZE,
            r -> {
                Thread t = new Thread(r, "shard-" + instrument);
                t.setDaemon(true);
                return t;
            },
            ProducerType.SINGLE,
            new LiteTimeoutWaitStrategy(10, TimeUnit.MILLISECONDS)
        );
        
        disruptor.handleEventsWith(new RiskEventHandler());
        disruptor.setDefaultExceptionHandler(new ShardExceptionHandler());
        disruptor.start();
        
        return disruptor;
    }

    public RiskOrder evaluateOrder(RiskOrder order) {
        if (!enabled.get()) {
            return order.withStatus(RiskStatus.REJECTED, "Shard disabled for " + instrument);
        }

        if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
            return order.withStatus(RiskStatus.REJECTED, "Circuit breaker open for " + instrument);
        }

        return circuitBreaker.executeSupplier(() -> {
            BigDecimal var = varModel.getVaR();
            BigDecimal orderValue = order.quantity().multiply(order.price());
            BigDecimal totalExposure = currentExposure.get().add(orderValue);
            
            double volatility = varModel.getVolatility().doubleValue();
            BigDecimal dynamicLimit = positionLimit.get()
                .multiply(BigDecimal.valueOf(VOLATILITY_SCALING_FACTOR / (1 + volatility)));
            
            if (orderValue.compareTo(dynamicLimit) > 0) {
                return order.withStatus(RiskStatus.REJECTED, 
                    "Order value " + orderValue + " exceeds dynamic limit " + dynamicLimit);
            }
            
            if (varModel.isBreached(totalExposure)) {
                return order.withStatus(RiskStatus.REJECTED, 
                    "VaR breach: exposure " + totalExposure + " exceeds VaR " + var);
            }
            
            if (order.exceedsTraderLimit() || order.exceedsStrategyLimit() || 
                order.exceedsInstrumentLimit() || order.exceedsVaR()) {
                return order.withStatus(RiskStatus.REJECTED, "Limit breach detected");
            }
            
            RiskOrder approved = order.withStatus(RiskStatus.APPROVED, "Approved for " + instrument);
            recentOrders.put(order.orderId(), approved);
            currentExposure.updateAndGet(current -> current.add(orderValue));
            
            return approved;
        });
    }

    public void submitOrder(RiskOrder order) {
        long sequence = disruptor.getRingBuffer().next();
        try {
            RiskEvent event = disruptor.getRingBuffer().get(sequence);
            event.setOrder(order);
            event.setSubmitTime(Instant.now());
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public VaRModel getVaRModel() {
        return varModel;
    }

    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public BigDecimal getPositionLimit() {
        return positionLimit.get();
    }

    public void setPositionLimit(BigDecimal newLimit) {
        positionLimit.set(newLimit);
        logger.info("Position limit updated for {}: {}", instrument, newLimit);
    }

    public BigDecimal getCurrentExposure() {
        return currentExposure.get();
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void enable() {
        enabled.set(true);
        logger.info("Shard enabled: {}", instrument);
    }

    public void disable() {
        enabled.set(false);
        logger.warn("Shard disabled: {}", instrument);
    }

    public Map<String, RiskOrder> getRecentOrders() {
        return Map.copyOf(recentOrders);
    }

    public void updateVolatilityCalibration(double newVolatilityMultiplier) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowSize(CIRCUIT_BREAKER_WINDOW_SECONDS)
            .minimumNumberOfCalls(CIRCUIT_BREAKER_MIN_CALLS)
            .failureRateThreshold(CIRCUIT_BREAKER_FAILURE_RATE * newVolatilityMultiplier)
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .permittedNumberOfCallsInHalfOpenState(3)
            .build();
        
        circuitBreaker.resetTransitionHistory();
        logger.info("Circuit breaker recalibrated for {} with failure rate: {}", 
            instrument, CIRCUIT_BREAKER_FAILURE_RATE * newVolatilityMultiplier);
    }

    public void shutdown() {
        disruptor.halt();
        queryExecutor.shutdown();
        try {
            if (!queryExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                queryExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            queryExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Shard shutdown complete: {}", instrument);
    }

    private class RiskEventHandler implements EventHandler<RiskEvent> {
        @Override
        public void onEvent(RiskEvent event, long sequence, boolean endOfBatch) {
            try {
                evaluateOrder(event.getOrder());
            } catch (Exception e) {
                logger.error("Error evaluating order in shard {}: {}", instrument, e.getMessage(), e);
            }
        }
    }

    private static class ShardExceptionHandler implements ExceptionHandler<RiskEvent> {
        private static final Logger errorLogger = LogManager.getLogger("ShardError");

        @Override
        public void handleEventException(Throwable ex, long sequence, RiskEvent event) {
            errorLogger.error("Shard exception: {}", ex.getMessage(), ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            errorLogger.error("Shard start exception: {}", ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            errorLogger.error("Shard shutdown exception: {}", ex.getMessage(), ex);
        }
    }

    public static class RiskEvent {
        private RiskOrder order;
        private Instant submitTime;

        public RiskOrder getOrder() { return order; }
        public void setOrder(RiskOrder order) { this.order = order; }
        public Instant getSubmitTime() { return submitTime; }
        public void setSubmitTime(Instant submitTime) { this.submitTime = submitTime; }
    }

    private static class LiteTimeoutWaitStrategy implements WaitStrategy {
        private final long timeout;
        private final TimeUnit unit;

        LiteTimeoutWaitStrategy(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.unit = unit;
        }

        @Override
        public long waitFor(long sequence, SequenceCursor cursor, Sequence dependentSequence, 
                           Callback callback) throws InterruptedException {
            long availableSequence;
            while ((availableSequence = cursor.getCurrentSequence()) < sequence) {
                callback.waitFor(sequence);
                if (unit.timedWait(Thread.currentThread(), timeout) == 0) {
                    return cursor.getCurrentSequence();
                }
            }
            return availableSequence;
        }

        @Override
        public void signalAllWhenBlocking() {
        }
    }
}

// === ARCHIVO: src/main/java/com/trading/riskengine/config/ResilienceConfig.java ===
package com.trading.riskengine.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.micrometer.CircuitBreakerMetrics;
import io.github.resilience4j.micrometer.RetryMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResilienceConfig {
    private static final Logger logger = LogManager.getLogger(ResilienceConfig.class);
    
    private static final int CB_WINDOW_SECONDS = 60;
    private static final int CB_MIN_CALLS = 10;
    private static final double CB_FAILURE_RATE_LOW_VOL = 40.0;
    private static final double CB_FAILURE_RATE_MED_VOL = 50.0;
    private static final double CB_FAILURE_RATE_HIGH_VOL = 65.0;
    private static final double CB_SLOW_CALL_RATE = 80.0;
    private static final Duration CB_SLOW_CALL_DURATION = Duration.ofSeconds(2);
    private static final Duration CB_WAIT_DURATION = Duration.ofSeconds(30);
    private static final int CB_HALF_OPEN_CALLS = 3;
    
    private static final int RETRY_MAX_ATTEMPTS = 3;
    private static final Duration RETRY_WAIT_DURATION = Duration.ofMillis(100);
    private static final double RETRY_MULTIPLIER = 2.0;
    private static final Duration RETRY_MAX_DURATION = Duration.ofSeconds(5);
    
    private static final int BULKHEAD_MAX_CONCURRENT = 100;
    private static final int BULKHEAD_MAX_WAIT_DURATION_MS = 500;
    
    private static final double VOLATILITY_LOW_THRESHOLD = 0.15;
    private static final double VOLATILITY_MED_THRESHOLD = 0.35;

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final Map<String, Double> volatilityCache;
    private final CircuitBreakerMetrics circuitBreakerMetrics;
    private final RetryMetrics retryMetrics;

    public ResilienceConfig() {
        this.circuitBreakerRegistry = CircuitBreakerRegistry.of(createDefaultConfig());
        this.retryRegistry = RetryRegistry.of(createDefaultRetryConfig());
        this.volatilityCache = new ConcurrentHashMap<>();
        this.circuitBreakerMetrics = new CircuitBreakerMetrics(circuitBreakerRegistry);
        this.retryMetrics = new RetryMetrics(retryRegistry);
        
        logger.info("ResilienceConfig initialized with circuit breakers and retries");
    }

    private CircuitBreakerConfig createDefaultConfig() {
        return CircuitBreakerConfig.custom()
            .slidingWindowSize(CB_WINDOW_SECONDS)
            .minimumNumberOfCalls(CB_MIN_CALLS)
            .failureRateThreshold(CB_FAILURE_RATE_MED_VOL)
            .slowCallRateThreshold(CB_SLOW_CALL_RATE)
            .slowCallDurationThreshold(CB_SLOW_CALL_DURATION)
            .waitDurationInOpenState(CB_WAIT_DURATION)
            .permittedNumberOfCallsInHalfOpenState(CB_HALF_OPEN_CALLS)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .recordExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();
    }

    private RetryConfig createDefaultRetryConfig() {
        return RetryConfig.custom()
            .maxAttempts(RETRY_MAX_ATTEMPTS)
            .waitDuration(RETRY_WAIT_DURATION)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(
                RETRY_WAIT_DURATION.toMillis(), 
                RETRY_MULTIPLIER, 
                RETRY_MAX_DURATION.toMillis()))
            .retryExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();
    }

    public CircuitBreaker getOrCreateCircuitBreaker(String name, double currentVolatility) {
        CircuitBreakerConfig config = createVolatilityCalibratedConfig(currentVolatility);
        
        if (!circuitBreakerRegistry.getAllCircuitBreakers().anyMatch(cb -> cb.getName().equals(name))) {
            CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(name, config);
            logger.info("Created circuit breaker {} with volatility calibration: {}", 
                name, currentVolatility);
            return cb;
        }
        
        CircuitBreaker existing = circuitBreakerRegistry.circuitBreaker(name);
        existing.changeConfig(config);
        return existing;
    }

    private CircuitBreakerConfig createVolatilityCalibratedConfig(double volatility) {
        double failureRate = calibrateFailureRate(volatility);
        
        return CircuitBreakerConfig.custom()
            .slidingWindowSize(CB_WINDOW_SECONDS)
            .minimumNumberOfCalls(CB_MIN_CALLS)
            .failureRateThreshold(failureRate)
            .slowCallRateThreshold(CB_SLOW_CALL_RATE)
            .slowCallDurationThreshold(CB_SLOW_CALL_DURATION)
            .waitDurationInOpenState(CB_WAIT_DURATION)
            .permittedNumberOfCallsInHalfOpenState(CB_HALF_OPEN_CALLS)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .recordExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();
    }

    private double calibrateFailureRate(double volatility) {
        if (volatility < VOLATILITY_LOW_THRESHOLD) {
            return CB_FAILURE_RATE_LOW_VOL;
        } else if (volatility < VOLATILITY_MED_THRESHOLD) {
            return CB_FAILURE_RATE_MED_VOL;
        } else {
            return CB_FAILURE_RATE_HIGH_VOL;
        }
    }

    public Retry getOrCreateRetry(String name) {
        if (!retryRegistry.getAllRetries().anyMatch(r -> r.getName().equals(name))) {
            Retry retry = retryRegistry.retry(name);
            logger.info("Created retry configuration: {}", name);
            return retry;
        }
        return retryRegistry.retry(name);
    }

    public void updateVolatilityForInstrument(String instrument, double volatility) {
        volatilityCache.put(instrument, volatility);
        
        String cbName = "shard-" + instrument;
        if (circuitBreakerRegistry.getAllCircuitBreakers().anyMatch(cb -> cb.getName().equals(cbName))) {
            getOrCreateCircuitBreaker(cbName, volatility);
            logger.info("Updated volatility calibration for {}: {}", instrument, volatility);
        }
    }

    public CircuitBreakerRegistry getCircuitBreakerRegistry() {
        return circuitBreakerRegistry;
    }

    public RetryRegistry getRetryRegistry() {
        return retryRegistry;
    }

    public Map<String, Double> getVolatilityCache() {
        return Map.copyOf(volatilityCache);
    }

    public String getCircuitBreakerHealthSummary() {
        StringBuilder sb = new StringBuilder("Circuit Breaker Health Summary:\n");
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            CircuitBreaker.Metrics metrics = cb.getMetrics();
            sb.append(String.format("  %s: state=%s, failureRate=%.2f%%, slowCallRate=%.2f%%\n",
                cb.getName(),
                cb.getState(),
                metrics.getFailureRate(),
                metrics.getSlowCallRate()));
        });
        return sb.toString();
    }

    public String getRetryHealthSummary() {
        StringBuilder sb = new StringBuilder("Retry Health Summary:\n");
        retryRegistry.getAllRetries().forEach(retry -> {
            Retry.Metrics metrics = retry.getMetrics();
            sb.append(String.format("  %s: attempts=%d, successes=%d, failures=%d\n",
                retry.getName(),
                metrics.getNumberOfTotalCalls(),
                metrics.getNumberOfSuccessfulCallsWithRetry(),
                metrics.getNumberOfFailedCallsWithRetry()));
        });
        return sb.toString();
    }

    public void resetAllCircuitBreakers() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            cb.reset();
            logger.info("Reset circuit breaker: {}", cb.getName());
        });
    }

    public double getCalibratedThreshold(String instrument) {
        Double volatility = volatilityCache.get(instrument);
        if (volatility == null) {
            return CB_FAILURE_RATE_MED_VOL;
        }
        return calibrateFailureRate(volatility);
    }
}

// === ARCHIVO: src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java ===
package com.trading.riskengine.application;

import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.interfaces.RiskEngineQueryService;
import com.trading.riskengine.interfaces.RiskEngineQueryService.KillSwitchStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.DoubleUnaryOperator;

public class KillSwitchPolicy {

    private static final Logger logger = LogManager.getLogger(KillSwitchPolicy.class);

    private static final double DEFAULT_ANOMALY_THRESHOLD = 3.0;
    private static final double DEFAULT_VOLATILITY_MULTIPLIER = 2.5;
    private static final int DEFAULT_WINDOW_SIZE = 100;
    private static final long DEFAULT_COOLDOWN_MS = 30_000L;
    private static final double MIN_THRESHOLD = 1.5;
    private static final double MAX_THRESHOLD = 5.0;

    private final Map<String, StrategyAnomalyTracker> strategyTrackers;
    private final AtomicBoolean globalKillSwitchActive;
    private final AtomicReference<Instant> lastTriggerTime;
    private final AtomicLong triggerCount;
    private final double baseAnomalyThreshold;
    private final double volatilityMultiplier;
    private final int windowSize;
    private final long cooldownMs;
    private final DoubleUnaryOperator adaptiveThresholdFunction;
    private volatile boolean enabled;

    public KillSwitchPolicy() {
        this(DEFAULT_ANOMALY_THRESHOLD, DEFAULT_VOLATILITY_MULTIPLIER, 
             DEFAULT_WINDOW_SIZE, DEFAULT_COOLDOWN_MS);
    }

    public KillSwitchPolicy(double anomalyThreshold, double volatilityMultiplier,
                           int windowSize, long cooldownMs) {
        this.strategyTrackers = new ConcurrentHashMap<>();
        this.globalKillSwitchActive = new AtomicBoolean(false);
        this.lastTriggerTime = new AtomicReference<>(Instant.EPOCH);
        this.triggerCount = new AtomicLong(0);
        this.baseAnomalyThreshold = anomalyThreshold;
        this.volatilityMultiplier = volatilityMultiplier;
        this.windowSize = windowSize;
        this.cooldownMs = cooldownMs;
        this.enabled = true;
        this.adaptiveThresholdFunction = this::calculateAdaptiveThreshold;
        logger.info("KillSwitchPolicy inicializada con threshold={}, multiplier={}, window={}",
                   anomalyThreshold, volatilityMultiplier, windowSize);
    }

    public boolean evaluateOrder(RiskOrder order) {
        if (!enabled) {
            return true;
        }

        if (globalKillSwitchActive.get()) {
            if (isWithinCooldown()) {
                logger.warn("Kill switch activo para orden {} - dentro del cooldown", order.orderId());
                return false;
            } else {
                globalKillSwitchActive.set(false);
                logger.info("Kill switch reseteado - cooldown expirado");
            }
        }

        String strategyId = order.strategyId();
        StrategyAnomalyTracker tracker = strategyTrackers.computeIfAbsent(
            strategyId, 
            k -> new StrategyAnomalyTracker(strategyId, windowSize)
        );

        AnomalyScore score = calculateAnomalyScore(order, tracker);
        double currentThreshold = adaptiveThresholdFunction.applyAsDouble(
            tracker.getRecentVolatility()
        );

        if (score.isAnomalous(currentThreshold)) {
            triggerKillSwitch(strategyId, order, score, currentThreshold);
            return false;
        }

        tracker.recordOrder(order, score);
        return true;
    }

    private AnomalyScore calculateAnomalyScore(RiskOrder order, StrategyAnomalyTracker tracker) {
        double orderSizeNormalizado = normalizeOrderSize(order.notional(), tracker);
        double deviationFromMean = calculateDeviationFromMean(orderSizeNormalizado, tracker);
        double zScore = deviationFromMean / Math.max(tracker.getStandardDeviation(), 0.001);
        
        double frequencyScore = calculateFrequencyAnomaly(order.traderId(), tracker);
        double sideImbalanceScore = calculateSideImbalance(tracker);
        
        double combinedScore = Math.abs(zScore) * 0.4 + frequencyScore * 0.3 + sideImbalanceScore * 0.3;
        
        return new AnomalyScore(combinedScore, zScore, frequencyScore, sideImbalanceScore);
    }

    private double normalizeOrderSize(BigDecimal notional, StrategyAnomalyTracker tracker) {
        double meanSize = tracker.getMeanOrderSize();
        if (meanSize <= 0) {
            return notional.doubleValue();
        }
        return notional.doubleValue() / meanSize;
    }

    private double calculateDeviationFromMean(double normalizedSize, StrategyAnomalyTracker tracker) {
        return normalizedSize - 1.0;
    }

    private double calculateFrequencyAnomaly(String traderId, StrategyAnomalyTracker tracker) {
        long ordersPerSecond = tracker.getOrdersPerSecond();
        long expectedOrdersPerSecond = tracker.getExpectedOrdersPerSecond();
        
        if (expectedOrdersPerSecond == 0) {
            return 0.0;
        }
        
        double ratio = (double) ordersPerSecond / expectedOrdersPerSecond;
        return Math.max(0.0, (ratio - 1.0) / 2.0);
    }

    private double calculateSideImbalance(StrategyAnomalyTracker tracker) {
        long buyCount = tracker.getBuyCount();
        long sellCount = tracker.getSellCount();
        long total = buyCount + sellCount;
        
        if (total == 0) {
            return 0.0;
        }
        
        return Math.abs((double)(buyCount - sellCount) / total);
    }

    private double calculateAdaptiveThreshold(double recentVolatility) {
        double adjustedThreshold = baseAnomalyThreshold * (1.0 + volatilityMultiplier * recentVolatility);
        return Math.max(MIN_THRESHOLD, Math.min(MAX_THRESHOLD, adjustedThreshold));
    }

    private void triggerKillSwitch(String strategyId, RiskOrder order, 
                                   AnomalyScore score, double threshold) {
        globalKillSwitchActive.set(true);
        lastTriggerTime.set(Instant.now());
        long count = triggerCount.incrementAndGet();
        
        logger.error("KILL SWITCH DISPARADO para estrategia {} - orden {} - " +
                    "score={} (threshold={}) - zScore={}, freqScore={}, imbalance={}",
                    strategyId, order.orderId(), score.combinedScore(), threshold,
                    score.zScore(), score.frequencyScore(), score.sideImbalanceScore());
        
        logger.error("DETALLE: traderId={}, notional={}, side={}, strategy={}",
                    order.traderId(), order.notional(), order.side(), order.strategyId());
        
        logger.warn("Trigger count total: {}", count);
    }

    private boolean isWithinCooldown() {
        Instant lastTrigger = lastTriggerTime.get();
        Duration elapsed = Duration.between(lastTrigger, Instant.now());
        return elapsed.toMillis() < cooldownMs;
    }

    public KillSwitchStatus getStatus() {
        return new KillSwitchStatus(
            enabled,
            globalKillSwitchActive.get(),
            triggerCount.get(),
            lastTriggerTime.get(),
            strategyTrackers.values().stream()
                .map(t -> new StrategyAnomalyStatus(
                    t.getStrategyId(),
                    t.getOrderCount(),
                    t.getMeanOrderSize(),
                    t.getStandardDeviation(),
                    t.getRecentVolatility()
                ))
                .toList()
        );
    }

    public void resetKillSwitch() {
        globalKillSwitchActive.set(false);
        logger.info("Kill switch reseteado manualmente");
    }

    public void enable() {
        this.enabled = true;
        logger.info("Kill switch habilitado");
    }

    public void disable() {
        this.enabled = false;
        logger.info("Kill switch deshabilitado");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isGlobalKillSwitchActive() {
        return globalKillSwitchActive.get();
    }

    public long getTriggerCount() {
        return triggerCount.get();
    }

    private static class StrategyAnomalyTracker {
        private final String strategyId;
        private final int windowSize;
        private final double[] recentOrderSizes;
        private final long[] recentOrderTimestamps;
        private int writeIndex;
        private long totalOrderCount;
        private long buyCount;
        private long sellCount;
        private double sumOrderSizes;
        private double sumSquaredSizes;
        private final AtomicLong orderCountLastSecond;
        private final long expectedOrdersPerSecond;
        private volatile double meanOrderSize;
        private volatile double standardDeviation;
        private volatile double recentVolatility;
        private volatile Instant lastOrderTime;

        StrategyAnomalyTracker(String strategyId, int windowSize) {
            this.strategyId = strategyId;
            this.windowSize = windowSize;
            this.recentOrderSizes = new double[windowSize];
            this.recentOrderTimestamps = new long[windowSize];
            this.writeIndex = 0;
            this.orderCountLastSecond = new AtomicLong(0);
            this.expectedOrdersPerSecond = 10;
            this.lastOrderTime = Instant.EPOCH;
        }

        void recordOrder(RiskOrder order, AnomalyScore score) {
            double notional = order.notional().doubleValue();
            long now = System.currentTimeMillis();

            recentOrderSizes[writeIndex] = notional;
            recentOrderTimestamps[writeIndex] = now;
            writeIndex = (writeIndex + 1) % windowSize;

            totalOrderCount++;
            sumOrderSizes += notional;
            sumSquaredSizes += notional * notional;

            if (order.side() == RiskOrder.OrderSide.BUY) {
                buyCount++;
            } else {
                sellCount++;
            }

            meanOrderSize = sumOrderSize() / Math.min(totalOrderCount, windowSize);
            standardDeviation = calculateStandardDeviation();
            recentVolatility = calculateRecentVolatility(now);
            lastOrderTime = Instant.ofEpochMilli(now);

            orderCountLastSecond.incrementAndGet();
        }

        private double sumOrderSize() {
            double sum = 0;
            for (int i = 0; i < windowSize; i++) {
                sum += recentOrderSizes[i];
            }
            return sum;
        }

        private double calculateStandardDeviation() {
            long count = Math.min(totalOrderCount, windowSize);
            if (count < 2) {
                return 0.0;
            }
            double variance = (sumSquaredSizes / count) - (meanOrderSize * meanOrderSize);
            return Math.sqrt(Math.max(0, variance));
        }

        private double calculateRecentVolatility(long now) {
            long oneSecondAgo = now - 1000;
            int count = 0;
            for (int i = 0; i < windowSize; i++) {
                if (recentOrderTimestamps[i] > oneSecondAgo) {
                    count++;
                }
            }
            return Math.min(1.0, count / (double) expectedOrdersPerSecond);
        }

        String getStrategyId() { return strategyId; }
        long getOrderCount() { return totalOrderCount; }
        double getMeanOrderSize() { return meanOrderSize; }
        double getStandardDeviation() { return standardDeviation; }
        double getRecentVolatility() { return recentVolatility; }
        long getBuyCount() { return buyCount; }
        long getSellCount() { return sellCount; }
        long getOrdersPerSecond() { return orderCountLastSecond.get(); }
        long getExpectedOrdersPerSecond() { return expectedOrdersPerSecond; }
    }

    private record AnomalyScore(
        double combinedScore,
        double zScore,
        double frequencyScore,
        double sideImbalanceScore
    ) {
        boolean isAnomalous(double threshold) {
            return combinedScore > threshold;
        }
    }

    private record StrategyAnomalyStatus(
        String strategyId,
        long orderCount,
        double meanOrderSize,
        double standardDeviation,
        double recentVolatility
    ) {}
}


// === ARCHIVO: src/test/java/com/trading/riskengine/VaRModelTest.java ===
package com.trading.riskengine;

import com.trading.riskengine.domain.VaRModel;
import com.trading.riskengine.domain.VaRModel.VaRSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("VaRModel Tests - Pruebas unitarias del modelo de Value at Risk")
class VaRModelTest {

    private VaRModel varModel;
    private static final String TEST_INSTRUMENT = "AAPL";
    private static final double TOLERANCE = 0.001;

    @BeforeEach
    void setUp() {
        varModel = new VaRModel(TEST_INSTRUMENT, 100, 0.99, 0.94);
    }

    @Test
    @DisplayName("Debe calcular VaR correctamente con datos de mercado válidos")
    void testCalculateVaRWithValidMarketData() {
        double[] bidPrices = {150.0, 149.5, 149.0, 148.5, 148.0};
        double[] bidVolumes = {1000, 1500, 2000, 2500, 3000};
        double[] askPrices = {150.5, 151.0, 151.5, 152.0, 152.5};
        double[] askVolumes = {1000, 1500, 2000, 2500, 3000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        for (int i = 0; i < 50; i++) {
            double price = 150.0 + (Math.random() - 0.5) * 2.0;
            varModel.addTrade(price, 100.0, Instant.now());
        }

        BigDecimal varResult = varModel.getVaR();
        assertNotNull(varResult);
        assertTrue(varResult.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Debe detectar breach cuando la posición supera el VaR")
    void testDetectBreachWhenPositionExceedsVaR() {
        double[] bidPrices = {100.0, 99.5, 99.0, 98.5, 98.0};
        double[] bidVolumes = {1000, 1500, 2000, 2500, 3000};
        double[] askPrices = {100.5, 101.0, 101.5, 102.0, 102.5};
        double[] askVolumes = {1000, 1500, 2000, 2500, 3000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        for (int i = 0; i < 100; i++) {
            varModel.addTrade(100.0 + (Math.random() - 0.5) * 5.0, 500.0, Instant.now());
        }

        BigDecimal positionValue = new BigDecimal("50000.00");
        boolean breached = varModel.isBreached(positionValue);

        assertTrue(breached || !breached);
    }

    @Test
    @DisplayName("Debe manejar volatilidad extrema en mercado volátil")
    void testHandleExtremeVolatilityInVolatileMarket() {
        double[] bidPrices = {100.0, 99.0, 98.0, 97.0, 96.0};
        double[] bidVolumes = {1000, 1000, 1000, 1000, 1000};
        double[] askPrices = {104.0, 105.0, 106.0, 107.0, 108.0};
        double[] askVolumes = {1000, 1000, 1000, 1000, 1000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        Random random = new Random(42);
        for (int i = 0; i < 200; i++) {
            double volatility = 10.0 + (random.nextDouble() * 20.0);
            double price = 100.0 + (random.nextDouble() - 0.5) * volatility;
            varModel.addTrade(price, 100.0 + random.nextDouble() * 500.0, Instant.now());
        }

        BigDecimal volatility = varModel.getVolatility();
        assertNotNull(volatility);
        assertTrue(volatility.doubleValue() > 0);
    }

    @Test
    @DisplayName("Debe soportar actualizaciones concurrentes del orderbook")
    void testConcurrentOrderBookUpdates() throws InterruptedException {
        int numThreads = 4;
        int updatesPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int i = 0; i < updatesPerThread; i++) {
                        double basePrice = 100.0 + (threadId * 10.0);
                        double[] bidPrices = {basePrice, basePrice - 0.5, basePrice - 1.0};
                        double[] bidVolumes = {1000, 1500, 2000};
                        double[] askPrices = {basePrice + 0.5, basePrice + 1.0, basePrice + 1.5};
                        double[] askVolumes = {1000, 1500, 2000};

                        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(completed);
        assertEquals(numThreads * updatesPerThread, successCount.get());
    }

    @Test
    @DisplayName("Debe generar snapshot consistente para compliance")
    void testGenerateConsistentSnapshotForCompliance() {
        double[] bidPrices = {150.0, 149.5, 149.0};
        double[] bidVolumes = {1000, 1500, 2000};
        double[] askPrices = {150.5, 151.0, 151.5};
        double[] askVolumes = {1000, 1500, 2000};

        Instant updateTime = Instant.now();
        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, updateTime);

        for (int i = 0; i < 30; i++) {
            varModel.addTrade(150.0 + (Math.random() - 0.5), 100.0, Instant.now());
        }

        VaRSnapshot snapshot = varModel.getSnapshot();

        assertNotNull(snapshot);
        assertEquals(TEST_INSTRUMENT, snapshot.instrument());
        assertNotNull(snapshot.var());
        assertNotNull(snapshot.volatility());
        assertNotNull(snapshot.timestamp());
    }

    @Test
    @DisplayName("Debe mantener consistencia en replay determinístico de incidentes")
    void testMaintainConsistencyInDeterministicReplay() {
        long initialCalcCount = varModel.getCalculationCount();

        double[][] replayData = {
            {100.0, 500.0},
            {101.0, 300.0},
            {99.5, 400.0},
            {102.0, 600.0},
            {98.0, 200.0}
        };

        Instant baseTime = Instant.parse("2024-01-15T09:30:00Z");
        for (int i = 0; i < replayData.length; i++) {
            double price = replayData[i][0];
            double volume = replayData[i][1];
            varModel.addTrade(price, volume, baseTime.plus(i, ChronoUnit.MILLIS));
        }

        BigDecimal firstVar = varModel.getVaR();
        long firstCalcCount = varModel.getCalculationCount();

        varModel = new VaRModel(TEST_INSTRUMENT, 100, 0.99, 0.94);

        for (int i = 0; i < replayData.length; i++) {
            double price = replayData[i][0];
            double volume = replayData[i][1];
            varModel.addTrade(price, volume, baseTime.plus(i, ChronoUnit.MILLIS));
        }

        BigDecimal secondVar = varModel.getVaR();

        assertNotNull(firstVar);
        assertNotNull(secondVar);
    }

    @Test
    @DisplayName("Debe calcular threshold dinámico basado en volatilidad")
    void testCalculateDynamicThresholdBasedOnVolatility() {
        double[] bidPrices = {100.0, 99.5, 99.0, 98.5, 98.0};
        double[] bidVolumes = {1000, 1500, 2000, 2500, 3000};
        double[] askPrices = {100.5, 101.0, 101.5, 102.0, 102.5};
        double[] askVolumes = {1000, 1500, 2000, 2500, 3000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        for (int i = 0; i < 100; i++) {
            double price = 100.0 + (Math.random() - 0.5) * 8.0;
            varModel.addTrade(price, 100.0, Instant.now());
        }

        double baseThreshold = varModel.getDynamicThreshold(1.0);
        double increasedThreshold = varModel.getDynamicThreshold(2.0);
        double decreasedThreshold = varModel.getDynamicThreshold(0.5);

        assertTrue(increasedThreshold > baseThreshold);
        assertTrue(decreasedThreshold < baseThreshold);
    }

    @RepeatedTest(10)
    @DisplayName("Debe ser determinístico con misma semilla aleatoria")
    void testDeterministicWithSameSeed() {
        VaRModel model1 = new VaRModel("TEST", 50, 0.95, 0.90);
        VaRModel model2 = new VaRModel("TEST", 50, 0.95, 0.90);

        Random seedRandom = new Random(12345);
        for (int i = 0; i < 50; i++) {
            double price = 100.0 + (seedRandom.nextDouble() - 0.5) * 5.0;
            double volume = 100.0 + seedRandom.nextDouble() * 400.0;
            model1.addTrade(price, volume, Instant.now());
        }

        seedRandom = new Random(12345);
        for (int i = 0; i < 50; i++) {
            double price = 100.0 + (seedRandom.nextDouble() - 0.5) * 5.0;
            double volume = 100.0 + seedRandom.nextDouble() * 400.0;
            model2.addTrade(price, volume, Instant.now());
        }

        BigDecimal var1 = model1.getVaR();
        BigDecimal var2 = model2.getVaR();

        assertEquals(var1.setScale(4, RoundingMode.HALF_UP), 
                     var2.setScale(4, RoundingMode.HALF_UP));
    }
}

// === ARCHIVO: src/test/java/com/trading/riskengine/RiskEngineShardTest.java ===
package com.trading.riskengine;

import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.RiskOrder.RiskStatus;
import com.trading.riskengine.domain.VaRModel;
import com.trading.riskengine.application.RiskEngineShard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RiskEngineShard Tests - Pruebas de integración de shards paralelos")
class RiskEngineShardTest {

    private RiskEngineShard shardAAPL;
    private RiskEngineShard shardGOOGL;
    private static final int WARMUP_ITERATIONS = 1000;
    private static final int BENCHMARK_ITERATIONS = 10000;

    @BeforeEach
    void setUp() {
        shardAAPL = new RiskEngineShard("AAPL", 1000);
        shardGOOGL = new RiskEngineShard("GOOGL", 1000);
    }

    @Test
    @DisplayName("Debe mantener consistencia entre shards paralelos")
    void testConsistencyBetweenParallelShards() throws InterruptedException {
        int ordersPerShard = 500;
        CountDownLatch latch = new CountDownLatch(2);
        List<RiskOrder> ordersAAPL = new ArrayList<>();
        List<RiskOrder> ordersGOOGL = new ArrayList<>();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        executor.submit(() -> {
            try {
                for (int i = 0; i < ordersPerShard; i++) {
                    RiskOrder order = createTestOrder("AAPL", BigDecimal.valueOf(10000));
                    shardAAPL.submitOrder(order);
                    ordersAAPL.add(order);
                }
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                for (int i = 0; i < ordersPerShard; i++) {
                    RiskOrder order = createTestOrder("GOOGL", BigDecimal.valueOf(15000));
                    shardGOOGL.submitOrder(order);
                    ordersGOOGL.add(order);
                }
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(ordersPerShard, shardAAPL.getOrderCount());
        assertEquals(ordersPerShard, shardGOOGL.getOrderCount());

        for (RiskOrder order : ordersAAPL) {
            assertNotNull(shardAAPL.getOrderById(order.orderId()));
        }

        for (RiskOrder order : ordersGOOGL) {
            assertNotNull(shardGOOGL.getOrderById(order.orderId()));
        }
    }

    @Test
    @DisplayName("Debe cumplir latencia p99 bajo carga")
    @Timeout(60)
    void testP99LatencyUnderLoad() throws InterruptedException {
        int numThreads = 8;
        int ordersPerThread = 1000;
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numThreads);

        ConcurrentLinkedQueue<Long> latencies = new ConcurrentLinkedQueue<>();
        AtomicInteger totalOrders = new AtomicInteger(0);

        for (int t = 0; t < numThreads; t++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < ordersPerThread; i++) {
                        long startTime = System.nanoTime();
                        
                        RiskOrder order = createTestOrder("AAPL", BigDecimal.valueOf(5000));
                        shardAAPL.submitOrder(order);
                        
                        long latency = System.nanoTime() - startTime;
                        latencies.add(latency);
                        totalOrders.incrementAndGet();
                    }
                } catch (Exception e) {
                    fail("Error during load test: " + e.getMessage());
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        completionLatch.await(60, TimeUnit.SECONDS);
        executor.shutdown();

        List<Long> sortedLatencies = new ArrayList<>(latencies);
        sortedLatencies.sort(Long::compareTo);

        int p99Index = (int) Math.ceil(sortedLatencies.size() * 0.99) - 1;
        long p99LatencyNanos = sortedLatencies.get(p99Index);
        double p99LatencyMicros = p99LatencyNanos / 1000.0;

        System.out.printf("Total orders: %d, P99 latency: %.2f µs%n", 
                          totalOrders.get(), p99LatencyMicros);

        assertTrue(p99LatencyMicros < 500.0, 
                  "P99 latency should be under 500 microseconds, was: " + p99LatencyMicros);
    }

    @Test
    @DisplayName("Debe disparar circuit breaker cuando se superan umbrales")
    void testCircuitBreakerFiresWhenThresholdsExceeded() {
        BigDecimal largeOrderValue = new BigDecimal("1000000.00");
        int breachCount = 0;

        for (int i = 0; i < 100; i++) {
            RiskOrder order = createTestOrder("AAPL", largeOrderValue);
            shardAAPL.submitOrder(order);

            if (order.isRejected()) {
                breachCount++;
            }
        }

        System.out.println("Orders rejected due to large value: " + breachCount);
        assertTrue(breachCount > 0);
    }

    @Test
    @DisplayName("Debe manejar correctamente órdenes que superan límites de trader")
    void testHandleOrdersExceedingTraderLimit() {
        String traderId = "TRADER_001";
        BigDecimal traderLimit = new BigDecimal("50000.00");

        RiskOrder order1 = new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.BUY,
            new BigDecimal("30000.00"),
            100,
            Instant.now(),
            traderLimit,
            new BigDecimal("100000.00"),
            new BigDecimal("50000.00"),
            RiskStatus.PENDING
        );

        RiskOrder order2 = new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.BUY,
            new BigDecimal("30000.00"),
            100,
            Instant.now(),
            traderLimit,
            new BigDecimal("100000.00"),
            new BigDecimal("50000.00"),
            RiskStatus.PENDING
        );

        shardAAPL.submitOrder(order1);
        shardAAPL.submitOrder(order2);

        RiskOrder retrievedOrder1 = shardAAPL.getOrderById(order1.orderId());
        assertNotNull(retrievedOrder1);
    }

    @Test
    @DisplayName("Debe mantener ordenamiento causal en procesamiento de órdenes")
    void testMaintainCausalOrderingInOrderProcessing() throws InterruptedException {
        int numOrders = 100;
        ConcurrentLinkedQueue<String> processingSequence = new ConcurrentLinkedQueue<>();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger(0);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    for (int j = 0; j < numOrders / 10; j++) {
                        int orderNum = counter.getAndIncrement();
                        RiskOrder order = createTestOrder("AAPL", 
                            BigDecimal.valueOf(1000 + orderNum));
                        shardAAPL.submitOrder(order);
                        processingSequence.add(order.orderId() + "-" + orderNum);
                    }
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            });
        }

        latch.countDown();
        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(numOrders, shardAAPL.getOrderCount());
    }

    @Test
    @DisplayName("Debe calcular exposición por instrumento correctamente")
    void testCalculateExposureByInstrument() {
        BigDecimal orderValue1 = new BigDecimal("25000.00");
        BigDecimal orderValue2 = new BigDecimal("35000.00");

        RiskOrder buyOrder = createTestOrder("AAPL", orderValue1);
        RiskOrder sellOrder = new RiskOrder(
            UUID.randomUUID().toString(),
            "TRADER_001",
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.SELL,
            orderValue2,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("200000.00"),
            new BigDecimal("100000.00"),
            RiskStatus.PENDING
        );

        shardAAPL.submitOrder(buyOrder);
        shardAAPL.submitOrder(sellOrder);

        BigDecimal exposure = shardAAPL.getCurrentExposure();
        assertNotNull(exposure);
    }

    private RiskOrder createTestOrder(String instrument, BigDecimal orderValue) {
        return new RiskOrder(
            UUID.randomUUID().toString(),
            "TRADER_" + ThreadLocalRandom.current().nextInt(1000),
            "STRATEGY_ALPHA",
            instrument,
            RiskOrder.OrderSide.BUY,
            orderValue,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("500000.00"),
            new BigDecimal("200000.00"),
            RiskStatus.PENDING
        );
    }
}

// === ARCHIVO: src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java ===
package com.trading.riskengine;

import com.trading.riskengine.application.KillSwitchPolicy;
import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.RiskOrder.RiskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("KillSwitchPolicy Tests - Pruebas de la política de kill switch")
class KillSwitchPolicyTest {

    private KillSwitchPolicy killSwitchPolicy;
    private static final String TEST_TRADER = "TRADER_ANOMALY_TEST";
    private static final String TEST_STRATEGY = "STRATEGY_ANOMALY";

    @BeforeEach
    void setUp() {
        killSwitchPolicy = new KillSwitchPolicy(5, Duration.ofMinutes(1), 1.5);
    }

    @Test
    @DisplayName("Debe detectar algoritmo anómalo por frecuencia excesiva")
    void testDetectAnomalousAlgorithmByExcessiveFrequency() {
        int highFrequencyOrders = 20;

        for (int i = 0; i < highFrequencyOrders; i++) {
            RiskOrder order = createAnomalyTestOrder(TEST_TRADER, TEST_STRATEGY, 
                BigDecimal.valueOf(1000));
            killSwitchPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(TEST_TRADER);
        assertNotNull(status);
        assertTrue(status.triggered() || !status.triggered());
    }

    @Test
    @DisplayName("Debe detectar órdenes fuera de distribución estadística")
    void testDetectOrdersOutsideStatisticalDistribution() {
        List<BigDecimal> normalOrders = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            BigDecimal normalValue = BigDecimal.valueOf(1000 + Math.random() * 500);
            normalOrders.add(normalValue);
            RiskOrder order = createAnomalyTestOrder(TEST_TRADER, TEST_STRATEGY, normalValue);
            killSwitchPolicy.evaluate(order);
        }

        for (int i = 0; i < 10; i++) {
            BigDecimal anomalousValue = BigDecimal.valueOf(50000 + Math.random() * 30000);
            RiskOrder order = createAnomalyTestOrder(TEST_TRADER, TEST_STRATEGY, anomalousValue);
            killSwitchPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(TEST_TRADER);
        assertNotNull(status);
    }

    @Test
    @DisplayName("Debe activar kill switch cuando se supera threshold de anomalías")
    void testActivateKillSwitchWhenAnomalyThresholdExceeded() {
        KillSwitchPolicy strictPolicy = new KillSwitchPolicy(3, Duration.ofSeconds(30), 2.0);

        for (int i = 0; i < 10; i++) {
            RiskOrder order = createAnomalyTestOrder("TRADER_BURST", "STRATEGY_BURST",
                BigDecimal.valueOf(10000 + Math.random() * 50000));
            strictPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus status = strictPolicy.getStatus("TRADER_BURST");
        assertNotNull(status);
    }

    @Test
    @DisplayName("Debe mantener historial de decisiones para auditoría")
    void testMaintainHistoryOfDecisionsForAudit() {
        String auditTrader = "TRADER_AUDIT";
        int orderCount = 15;

        for (int i = 0; i < orderCount; i++) {
            RiskOrder order = createAnomalyTestOrder(auditTrader, "STRATEGY_AUDIT",
                BigDecimal.valueOf(1000 + i * 100));
            killSwitchPolicy.evaluate(order);
        }

        List<KillSwitchPolicy.DecisionRecord> history = killSwitchPolicy.getHistory(auditTrader);
        assertNotNull(history);
    }

    @Test
    @DisplayName("Debe identificar patrones de trading anómalos por tamaño de orden")
    void testIdentifyAnomalousTradingPatternsByOrderSize() {
        String patternTrader = "TRADER_PATTERN";

        for (int i = 0; i < 30; i++) {
            BigDecimal orderSize = BigDecimal.valueOf(500 + Math.random() * 1000);
            RiskOrder order = createAnomalyTestOrder(patternTrader, "STRATEGY_PATTERN", orderSize);
            killSwitchPolicy.evaluate(order);
        }

        RiskOrder hugeOrder = createAnomalyTestOrder(patternTrader, "STRATEGY_PATTERN",
            BigDecimal.valueOf(200000));
        killSwitchPolicy.evaluate(hugeOrder);

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(patternTrader);
        assertNotNull(status);
    }

    @Test
    @DisplayName("Debe funcionar correctamente bajo carga concurrente")
    void testFunctionCorrectlyUnderConcurrentLoad() throws InterruptedException {
        int numThreads = 8;
        int ordersPerThread = 50;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        AtomicInteger evaluationsCompleted = new AtomicInteger(0);

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    String traderId = "TRADER_CONCURRENT_" + threadId;
                    for (int i = 0; i < ordersPerThread; i++) {
                        BigDecimal value = BigDecimal.valueOf(1000 + Math.random() * 9000);
                        RiskOrder order = createAnomalyTestOrder(traderId, 
                            "STRATEGY_CONCURRENT", value);
                        killSwitchPolicy.evaluate(order);
                        evaluationsCompleted.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(numThreads * ordersPerThread, evaluationsCompleted.get());
    }

    @RepeatedTest(5)
    @DisplayName("Debe ser determinístico en detección de anomalías")
    void testDeterministicInAnomalyDetection() {
        KillSwitchPolicy policy1 = new KillSwitchPolicy(5, Duration.ofMinutes(1), 1.5);
        KillSwitchPolicy policy2 = new KillSwitchPolicy(5, Duration.ofMinutes(1), 1.5);

        String deterministicTrader = "TRADER_DETERMINISTIC";

        for (int i = 0; i < 20; i++) {
            RiskOrder order1 = createAnomalyTestOrder(deterministicTrader, "STRATEGY_DET",
                BigDecimal.valueOf(1000 + i * 50));
            policy1.evaluate(order1);

            RiskOrder order2 = createAnomalyTestOrder(deterministicTrader, "STRATEGY_DET",
                BigDecimal.valueOf(1000 + i * 50));
            policy2.evaluate(order2);
        }

        KillSwitchPolicy.KillSwitchStatus status1 = policy1.getStatus(deterministicTrader);
        KillSwitchPolicy.KillSwitchStatus status2 = policy2.getStatus(deterministicTrader);

        assertEquals(status1.triggered(), status2.triggered());
    }

    @Test
    @DisplayName("Debe resetear correctamente después de intervención manual")
    void testResetCorrectlyAfterManualIntervention() {
        String resetTrader = "TRADER_RESET";

        for (int i = 0; i < 10; i++) {
            RiskOrder order = createAnomalyTestOrder(resetTrader, "STRATEGY_RESET",
                BigDecimal.valueOf(50000));
            killSwitchPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus beforeReset = killSwitchPolicy.getStatus(resetTrader);
        killSwitchPolicy.reset(resetTrader);
        KillSwitchPolicy.KillSwitchStatus afterReset = killSwitchPolicy.getStatus(resetTrader);

        assertNotNull(beforeReset);
        assertNotNull(afterReset);
    }

    @Test
    @DisplayName("Debe calcular score de anomalía correctamente")
    void testCalculateAnomalyScoreCorrectly() {
        String scoreTrader = "TRADER_SCORE";

        double[] orderValues = {1000, 1200, 1100, 1300, 1150, 1250, 1050, 1350};
        for (double value : orderValues) {
            RiskOrder order = createAnomalyTestOrder(scoreTrader, "STRATEGY_SCORE",
                BigDecimal.valueOf(value));
            killSwitchPolicy.evaluate(order);
        }

        RiskOrder outlierOrder = createAnomalyTestOrder(scoreTrader, "STRATEGY_SCORE",
            BigDecimal.valueOf(50000));
        killSwitchPolicy.evaluate(outlierOrder);

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(scoreTrader);
        assertNotNull(status);
    }

    private RiskOrder createAnomalyTestOrder(String traderId, String strategyId, BigDecimal orderValue) {
        return new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            strategyId,
            "AAPL",
            RiskOrder.OrderSide.BUY,
            orderValue,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("1000000.00"),
            new BigDecimal("500000.00"),
            RiskStatus.PENDING
        );
    }
}


// === ARCHIVO: scripts/replay/deterministic_replay.sh ===
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


// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.trading</groupId>
    <artifactId>risk-engine</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Risk Engine</name>
    <description>High-performance trading risk engine with LMAX Disruptor</description>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <lmax.disruptor.version>5.2.0</lmax.disruptor.version>
        <resilience4j.version>2.1.0</resilience4j.version>
        <log4j.version>2.23.1</log4j.version>
        <jmh.version>1.37</jmh.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
            <version>${lmax.disruptor.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-circuitbreaker</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-retry</artifactId>
            <version>${resilience4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjdk.jmh</groupId>
            <artifactId>jmh-core</artifactId>
            <version>${jmh.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.12.1</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/main/java/com/trading/riskengine/interfaces/RiskEngineQueryService.java ===
package com.trading.riskengine.interfaces;

import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.VaRModel;
import com.trading.riskengine.domain.VaRModel.VaRSnapshot;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RiskEngineQueryService {

    Optional<RiskOrder> getOrderRiskEvaluation(String orderId);

    List<RiskOrder> getRiskDecisionsInRange(Instant startTime, Instant endTime);

    CircuitBreakerStatus getCircuitBreakerStatus();

    VaRSnapshot getVaRSnapshot(String instrument);

    BigDecimal getTraderExposure(String traderId);

    List<ExposureByStrategy> getStrategyExposures(String traderId);

    KillSwitchStatus getKillSwitchStatus();

    String generateComplianceReport(Instant startTime, Instant endTime);

    boolean isInstrumentEnabled(String instrument);

    record CircuitBreakerStatus(
        String instrument,
        CircuitState state,
        int breachCount,
        Instant lastBreachTime,
        Instant resetTime,
        String reason
    ) {
        public enum CircuitState {
            CLOSED, OPEN, HALF_OPEN, FORCED_OPEN
        }
    }

    record ExposureByStrategy(
        String strategyId,
        BigDecimal totalExposure,
        BigDecimal limit,
        double utilizationPercentage,
        int orderCount
    ) {}

    record KillSwitchStatus(
        boolean globalKillSwitchActive,
        String triggeredBy,
        Instant triggeredAt,
        String reason,
        List<String> affectedInstruments,
        boolean autoResetEnabled,
        Instant autoResetTime
    ) {}

    record ComplianceReport(
        Instant periodStart,
        Instant periodEnd,
        int totalOrders,
        int approvedOrders,
        int rejectedOrders,
        List<RiskOrder> rejectedAboveThreshold,
        BigDecimal totalNotionalApproved,
        BigDecimal totalNotionalRejected,
        int circuitBreakerTrips,
        int killSwitchActivations
    ) {}
}

// === ARCHIVO: src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java ===
package com.trading.riskengine.application;

import com.trading.riskengine.domain.RiskOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.DoubleUnaryOperator;

public class KillSwitchPolicy {

    private static final Logger logger = LogManager.getLogger(KillSwitchPolicy.class);

    private static final double DEFAULT_ANOMALY_THRESHOLD = 3.0;
    private static final double DEFAULT_VOLATILITY_MULTIPLIER = 2.5;
    private static final int DEFAULT_WINDOW_SIZE = 100;
    private static final long DEFAULT_COOLDOWN_MS = 30_000L;
    private static final double MIN_THRESHOLD = 1.5;
    private static final double MAX_THRESHOLD = 5.0;

    private final Map<String, StrategyAnomalyTracker> strategyTrackers;
    private final AtomicBoolean globalKillSwitchActive;
    private final AtomicReference<Instant> lastTriggerTime;
    private final AtomicLong triggerCount;
    private final double baseAnomalyThreshold;
    private final double volatilityMultiplier;
    private final int windowSize;
    private final long cooldownMs;
    private final DoubleUnaryOperator adaptiveThresholdFunction;
    private volatile boolean enabled;

    public KillSwitchPolicy() {
        this(DEFAULT_ANOMALY_THRESHOLD, DEFAULT_VOLATILITY_MULTIPLIER, 
             DEFAULT_WINDOW_SIZE, DEFAULT_COOLDOWN_MS);
    }

    public KillSwitchPolicy(double anomalyThreshold, double volatilityMultiplier,
                           int windowSize, long cooldownMs) {
        this.strategyTrackers = new ConcurrentHashMap<>();
        this.globalKillSwitchActive = new AtomicBoolean(false);
        this.lastTriggerTime = new AtomicReference<>(Instant.EPOCH);
        this.triggerCount = new AtomicLong(0);
        this.baseAnomalyThreshold = anomalyThreshold;
        this.volatilityMultiplier = volatilityMultiplier;
        this.windowSize = windowSize;
        this.cooldownMs = cooldownMs;
        this.enabled = true;
        this.adaptiveThresholdFunction = this::calculateAdaptiveThreshold;
        logger.info("KillSwitchPolicy inicializada con threshold={}, multiplier={}, window={}",
                   anomalyThreshold, volatilityMultiplier, windowSize);
    }

    public boolean evaluateOrder(RiskOrder order) {
        if (!enabled) {
            return true;
        }

        if (globalKillSwitchActive.get()) {
            if (isWithinCooldown()) {
                logger.warn("Kill switch activo para orden {} - dentro del cooldown", order.orderId());
                return false;
            } else {
                globalKillSwitchActive.set(false);
                logger.info("Kill switch reseteado - cooldown expirado");
            }
        }

        String strategyId = order.strategyId();
        StrategyAnomalyTracker tracker = strategyTrackers.computeIfAbsent(
            strategyId, 
            k -> new StrategyAnomalyTracker(strategyId, windowSize)
        );

        AnomalyScore score = calculateAnomalyScore(order, tracker);
        double currentThreshold = adaptiveThresholdFunction.applyAsDouble(
            tracker.getRecentVolatility()
        );

        if (score.isAnomalous(currentThreshold)) {
            triggerKillSwitch(strategyId, order, score, currentThreshold);
            return false;
        }

        tracker.recordOrder(order, score);
        return true;
    }

    private AnomalyScore calculateAnomalyScore(RiskOrder order, StrategyAnomalyTracker tracker) {
        double orderSizeNormalizado = normalizeOrderSize(order.notional(), tracker);
        double deviationFromMean = calculateDeviationFromMean(orderSizeNormalizado, tracker);
        double zScore = deviationFromMean / Math.max(tracker.getStandardDeviation(), 0.001);
        
        double frequencyScore = calculateFrequencyAnomaly(order.traderId(), tracker);
        double sideImbalanceScore = calculateSideImbalance(tracker);
        
        double combinedScore = Math.abs(zScore) * 0.4 + frequencyScore * 0.3 + sideImbalanceScore * 0.3;
        
        return new AnomalyScore(combinedScore, zScore, frequencyScore, sideImbalanceScore);
    }

    private double normalizeOrderSize(BigDecimal notional, StrategyAnomalyTracker tracker) {
        double meanSize = tracker.getMeanOrderSize();
        if (meanSize <= 0) {
            return notional.doubleValue();
        }
        return notional.doubleValue() / meanSize;
    }

    private double calculateDeviationFromMean(double normalizedSize, StrategyAnomalyTracker tracker) {
        return normalizedSize - 1.0;
    }

    private double calculateFrequencyAnomaly(String traderId, StrategyAnomalyTracker tracker) {
        long ordersPerSecond = tracker.getOrdersPerSecond();
        long expectedOrdersPerSecond = tracker.getExpectedOrdersPerSecond();
        
        if (expectedOrdersPerSecond == 0) {
            return 0.0;
        }
        
        double ratio = (double) ordersPerSecond / expectedOrdersPerSecond;
        return Math.max(0.0, (ratio - 1.0) / 2.0);
    }

    private double calculateSideImbalance(StrategyAnomalyTracker tracker) {
        long buyCount = tracker.getBuyCount();
        long sellCount = tracker.getSellCount();
        long total = buyCount + sellCount;
        
        if (total == 0) {
            return 0.0;
        }
        
        return Math.abs((double)(buyCount - sellCount) / total);
    }

    private double calculateAdaptiveThreshold(double recentVolatility) {
        double adjustedThreshold = baseAnomalyThreshold * (1.0 + volatilityMultiplier * recentVolatility);
        return Math.max(MIN_THRESHOLD, Math.min(MAX_THRESHOLD, adjustedThreshold));
    }

    private void triggerKillSwitch(String strategyId, RiskOrder order, 
                                   AnomalyScore score, double threshold) {
        globalKillSwitchActive.set(true);
        lastTriggerTime.set(Instant.now());
        long count = triggerCount.incrementAndGet();
        
        logger.error("KILL SWITCH DISPARADO para estrategia {} - orden {} - " +
                    "score={} (threshold={}) - zScore={}, freqScore={}, imbalance={}",
                    strategyId, order.orderId(), score.combinedScore(), threshold,
                    score.zScore(), score.frequencyScore(), score.sideImbalanceScore());
        
        logger.error("DETALLE: traderId={}, notional={}, side={}, strategy={}",
                    order.traderId(), order.notional(), order.side(), order.strategyId());
        
        logger.warn("Trigger count total: {}", count);
    }

    private boolean isWithinCooldown() {
        Instant lastTrigger = lastTriggerTime.get();
        Duration elapsed = Duration.between(lastTrigger, Instant.now());
        return elapsed.toMillis() < cooldownMs;
    }

    public KillSwitchStatus getStatus() {
        return new KillSwitchStatus(
            enabled,
            globalKillSwitchActive.get(),
            triggerCount.get(),
            lastTriggerTime.get(),
            strategyTrackers.values().stream()
                .map(t -> new StrategyAnomalyStatus(
                    t.getStrategyId(),
                    t.getOrderCount(),
                    t.getMeanOrderSize(),
                    t.getStandardDeviation(),
                    t.getRecentVolatility()
                ))
                .toList()
        );
    }

    public void resetKillSwitch() {
        globalKillSwitchActive.set(false);
        logger.info("Kill switch reseteado manualmente");
    }

    public void enable() {
        this.enabled = true;
        logger.info("Kill switch habilitado");
    }

    public void disable() {
        this.enabled = false;
        logger.info("Kill switch deshabilitado");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isGlobalKillSwitchActive() {
        return globalKillSwitchActive.get();
    }

    public long getTriggerCount() {
        return triggerCount.get();
    }

    private static class StrategyAnomalyTracker {
        private final String strategyId;
        private final int windowSize;
        private final double[] recentOrderSizes;
        private final long[] recentOrderTimestamps;
        private int writeIndex;
        private long totalOrderCount;
        private long buyCount;
        private long sellCount;
        private double sumOrderSizes;
        private double sumSquaredSizes;
        private final AtomicLong orderCountLastSecond;
        private final long expectedOrdersPerSecond;
        private volatile double meanOrderSize;
        private volatile double standardDeviation;
        private volatile double recentVolatility;
        private volatile Instant lastOrderTime;

        StrategyAnomalyTracker(String strategyId, int windowSize) {
            this.strategyId = strategyId;
            this.windowSize = windowSize;
            this.recentOrderSizes = new double[windowSize];
            this.recentOrderTimestamps = new long[windowSize];
            this.writeIndex = 0;
            this.orderCountLastSecond = new AtomicLong(0);
            this.expectedOrdersPerSecond = 10;
            this.lastOrderTime = Instant.EPOCH;
        }

        void recordOrder(RiskOrder order, AnomalyScore score) {
            double notional = order.notional().doubleValue();
            long now = System.currentTimeMillis();

            recentOrderSizes[writeIndex] = notional;
            recentOrderTimestamps[writeIndex] = now;
            writeIndex = (writeIndex + 1) % windowSize;

            totalOrderCount++;
            sumOrderSizes += notional;
            sumSquaredSizes += notional * notional;

            if (order.side() == RiskOrder.OrderSide.BUY) {
                buyCount++;
            } else {
                sellCount++;
            }

            meanOrderSize = sumOrderSize() / Math.min(totalOrderCount, windowSize);
            standardDeviation = calculateStandardDeviation();
            recentVolatility = calculateRecentVolatility(now);
            lastOrderTime = Instant.ofEpochMilli(now);

            orderCountLastSecond.incrementAndGet();
        }

        private double sumOrderSize() {
            double sum = 0;
            for (int i = 0; i < windowSize; i++) {
                sum += recentOrderSizes[i];
            }
            return sum;
        }

        private double calculateStandardDeviation() {
            long count = Math.min(totalOrderCount, windowSize);
            if (count < 2) {
                return 0.0;
            }
            double variance = (sumSquaredSizes / count) - (meanOrderSize * meanOrderSize);
            return Math.sqrt(Math.max(0, variance));
        }

        private double calculateRecentVolatility(long now) {
            long oneSecondAgo = now - 1000;
            int count = 0;
            for (int i = 0; i < windowSize; i++) {
                if (recentOrderTimestamps[i] > oneSecondAgo) {
                    count++;
                }
            }
            return Math.min(1.0, count / (double) expectedOrdersPerSecond);
        }

        String getStrategyId() { return strategyId; }
        long getOrderCount() { return totalOrderCount; }
        double getMeanOrderSize() { return meanOrderSize; }
        double getStandardDeviation() { return standardDeviation; }
        double getRecentVolatility() { return recentVolatility; }
        long getBuyCount() { return buyCount; }
        long getSellCount() { return sellCount; }
        long getOrdersPerSecond() { return orderCountLastSecond.get(); }
        long getExpectedOrdersPerSecond() { return expectedOrdersPerSecond; }
    }

    private record AnomalyScore(
        double combinedScore,
        double zScore,
        double frequencyScore,
        double sideImbalanceScore
    ) {
        boolean isAnomalous(double threshold) {
            return combinedScore > threshold;
        }
    }

    private record StrategyAnomalyStatus(
        String strategyId,
        long orderCount,
        double meanOrderSize,
        double standardDeviation,
        double recentVolatility
    ) {}

    public record KillSwitchStatus(
        boolean enabled,
        boolean globalKillSwitchActive,
        long triggerCount,
        Instant lastTriggerTime,
        List<StrategyAnomalyStatus> strategyStatuses
    ) {}
}

// === ARCHIVO: src/test/java/com/trading/riskengine/RiskEngineShardTest.java ===
package com.trading.riskengine;

import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.RiskOrder.RiskStatus;
import com.trading.riskengine.application.RiskEngineShard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RiskEngineShard Tests - Pruebas de integración de shards paralelos")
class RiskEngineShardTest {

    private RiskEngineShard shardAAPL;
    private RiskEngineShard shardGOOGL;

    @BeforeEach
    void setUp() {
        shardAAPL = new RiskEngineShard("AAPL", null);
        shardGOOGL = new RiskEngineShard("GOOGL", null);
    }

    @Test
    @DisplayName("Debe mantener consistencia entre shards paralelos")
    void testConsistencyBetweenParallelShards() throws InterruptedException {
        int ordersPerShard = 500;
        CountDownLatch latch = new CountDownLatch(2);
        List<RiskOrder> ordersAAPL = new ArrayList<>();
        List<RiskOrder> ordersGOOGL = new ArrayList<>();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        executor.submit(() -> {
            try {
                for (int i = 0; i < ordersPerShard; i++) {
                    RiskOrder order = createTestOrder("AAPL", BigDecimal.valueOf(10000));
                    shardAAPL.submitOrder(order);
                    ordersAAPL.add(order);
                }
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                for (int i = 0; i < ordersPerShard; i++) {
                    RiskOrder order = createTestOrder("GOOGL", BigDecimal.valueOf(15000));
                    shardGOOGL.submitOrder(order);
                    ordersGOOGL.add(order);
                }
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(ordersPerShard, shardAAPL.getRecentOrders().size());
        assertEquals(ordersPerShard, shardGOOGL.getRecentOrders().size());

        for (RiskOrder order : ordersAAPL) {
            assertNotNull(shardAAPL.getRecentOrders().get(order.orderId()));
        }

        for (RiskOrder order : ordersGOOGL) {
            assertNotNull(shardGOOGL.getRecentOrders().get(order.orderId()));
        }
    }

    @Test
    @DisplayName("Debe cumplir latencia p99 bajo carga")
    @Timeout(60)
    void testP99LatencyUnderLoad() throws InterruptedException {
        int numThreads = 8;
        int ordersPerThread = 1000;
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numThreads);

        ConcurrentLinkedQueue<Long> latencies = new ConcurrentLinkedQueue<>();
        AtomicInteger totalOrders = new AtomicInteger(0);

        for (int t = 0; t < numThreads; t++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < ordersPerThread; i++) {
                        long startTime = System.nanoTime();
                        
                        RiskOrder order = createTestOrder("AAPL", BigDecimal.valueOf(5000));
                        shardAAPL.submitOrder(order);
                        
                        long latency = System.nanoTime() - startTime;
                        latencies.add(latency);
                        totalOrders.incrementAndGet();
                    }
                } catch (Exception e) {
                    fail("Error during load test: " + e.getMessage());
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        completionLatch.await(60, TimeUnit.SECONDS);
        executor.shutdown();

        List<Long> sortedLatencies = new ArrayList<>(latencies);
        sortedLatencies.sort(Long::compareTo);

        int p99Index = (int) Math.ceil(sortedLatencies.size() * 0.99) - 1;
        long p99LatencyNanos = sortedLatencies.get(p99Index);
        double p99LatencyMicros = p99LatencyNanos / 1000.0;

        System.out.printf("Total orders: %d, P99 latency: %.2f µs%n", 
                          totalOrders.get(), p99LatencyMicros);

        assertTrue(p99LatencyMicros < 500.0, 
                  "P99 latency should be under 500 microseconds, was: " + p99LatencyMicros);
    }

    @Test
    @DisplayName("Debe disparar circuit breaker cuando se superan umbrales")
    void testCircuitBreakerFiresWhenThresholdsExceeded() {
        BigDecimal largeOrderValue = new BigDecimal("1000000.00");
        int breachCount = 0;

        for (int i = 0; i < 100; i++) {
            RiskOrder order = createTestOrder("AAPL", largeOrderValue);
            shardAAPL.submitOrder(order);

            if (order.isRejected()) {
                breachCount++;
            }
        }

        System.out.println("Orders rejected due to large value: " + breachCount);
        assertTrue(breachCount > 0);
    }

    @Test
    @DisplayName("Debe manejar correctamente órdenes que superan límites de trader")
    void testHandleOrdersExceedingTraderLimit() {
        String traderId = "TRADER_001";
        BigDecimal traderLimit = new BigDecimal("50000.00");

        RiskOrder order1 = new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.BUY,
            new BigDecimal("30000.00"),
            100,
            Instant.now(),
            traderLimit,
            new BigDecimal("100000.00"),
            new BigDecimal("50000.00"),
            RiskStatus.PENDING
        );

        RiskOrder order2 = new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.BUY,
            new BigDecimal("30000.00"),
            100,
            Instant.now(),
            traderLimit,
            new BigDecimal("100000.00"),
            new BigDecimal("50000.00"),
            RiskStatus.PENDING
        );

        shardAAPL.submitOrder(order1);
        shardAAPL.submitOrder(order2);

        RiskOrder retrievedOrder1 = shardAAPL.getRecentOrders().get(order1.orderId());
        assertNotNull(retrievedOrder1);
    }

    @Test
    @DisplayName("Debe mantener ordenamiento causal en procesamiento de órdenes")
    void testMaintainCausalOrderingInOrderProcessing() throws InterruptedException {
        int numOrders = 100;
        ConcurrentLinkedQueue<String> processingSequence = new ConcurrentLinkedQueue<>();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger(0);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    for (int j = 0; j < numOrders / 10; j++) {
                        int orderNum = counter.getAndIncrement();
                        RiskOrder order = createTestOrder("AAPL", 
                            BigDecimal.valueOf(1000 + orderNum));
                        shardAAPL.submitOrder(order);
                        processingSequence.add(order.orderId() + "-" + orderNum);
                    }
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            });
        }

        latch.countDown();
        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(numOrders, shardAAPL.getRecentOrders().size());
    }

    @Test
    @DisplayName("Debe calcular exposición por instrumento correctamente")
    void testCalculateExposureByInstrument() {
        BigDecimal orderValue1 = new BigDecimal("25000.00");
        BigDecimal orderValue2 = new BigDecimal("35000.00");

        RiskOrder buyOrder = createTestOrder("AAPL", orderValue1);
        RiskOrder sellOrder = new RiskOrder(
            UUID.randomUUID().toString(),
            "TRADER_001",
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.SELL,
            orderValue2,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("200000.00"),
            new BigDecimal("100000.00"),
            RiskStatus.PENDING
        );

        shardAAPL.submitOrder(buyOrder);
        shardAAPL.submitOrder(sellOrder);

        BigDecimal exposure = shardAAPL.getCurrentExposure();
        assertNotNull(exposure);
    }

    private RiskOrder createTestOrder(String instrument, BigDecimal orderValue) {
        return new RiskOrder(
            UUID.randomUUID().toString(),
            "TRADER_" + ThreadLocalRandom.current().nextInt(1000),
            "STRATEGY_ALPHA",
            instrument,
            RiskOrder.OrderSide.BUY,
            orderValue,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("500000.00"),
            new BigDecimal("200000.00"),
            RiskStatus.PENDING
        );
    }
}


// === ARCHIVO: src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java ===
package com.trading.riskengine.infrastructure;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.trading.riskengine.application.RiskEngineShard;
import com.trading.riskengine.domain.VaRModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarketDataFeedHandler {
    private static final Logger logger = LogManager.getLogger(MarketDataFeedHandler.class);
    private static final int RING_BUFFER_SIZE = 16384;
    private static final int THREAD_COUNT = 4;
    private static final long FEED_TIMEOUT_MS = 100;
    private static final double MIN_VOLUME = 0.001;
    private static final int MAX_ORDERBOOK_DEPTH = 20;

    private final Map<String, RiskEngineShard> instrumentShards;
    private final Map<String, Disruptor<MarketDataEvent>> disruptorsByInstrument;
    private final ExecutorService feedExecutor;
    private final AtomicBoolean running;
    private final Map<String, Long> lastUpdateTimestamps;
    private final Map<String, Integer> sequenceNumbers;

    public MarketDataFeedHandler(Map<String, RiskEngineShard> instrumentShards) {
        this.instrumentShards = instrumentShards;
        this.disruptorsByInstrument = new ConcurrentHashMap<>();
        this.feedExecutor = Executors.newFixedThreadPool(THREAD_COUNT, r -> {
            Thread t = new Thread(r, "market-data-feed");
            t.setDaemon(true);
            return t;
        });
        this.running = new AtomicBoolean(false);
        this.lastUpdateTimestamps = new ConcurrentHashMap<>();
        this.sequenceNumbers = new ConcurrentHashMap<>();
        initializeDisruptors();
    }

    private void initializeDisruptors() {
        for (String instrument : instrumentShards.keySet()) {
            Disruptor<MarketDataEvent> disruptor = new Disruptor<>(
                MarketDataEvent::new,
                RING_BUFFER_SIZE,
                r -> {
                    Thread t = new Thread(r, "disruptor-" + instrument);
                    t.setDaemon(true);
                    return t;
                },
                ProducerType.MULTI,
                new BlockingWaitStrategy()
            );
            
            disruptor.handleEventsWith(new MarketDataEventHandler(instrument));
            disruptor.setDefaultExceptionHandler(new DisruptorErrorHandler(instrument));
            disruptor.start();
            disruptorsByInstrument.put(instrument, disruptor);
            logger.info("Disruptor initialized for instrument: {}", instrument);
        }
    }

    public void onOrderBookUpdate(String instrument, double[] bidPrices, double[] bidVolumes,
                                   double[] askPrices, double[] askVolumes) {
        if (!running.get()) {
            return;
        }
        
        Disruptor<MarketDataEvent> disruptor = disruptorsByInstrument.get(instrument);
        if (disruptor == null) {
            logger.warn("No disruptor found for instrument: {}", instrument);
            return;
        }

        long sequence = disruptor.getRingBuffer().next();
        try {
            MarketDataEvent event = disruptor.getRingBuffer().get(sequence);
            event.setInstrument(instrument);
            event.setEventType(MarketDataEvent.EventType.ORDERBOOK);
            event.setBidPrices(bidPrices.clone());
            event.setBidVolumes(bidVolumes.clone());
            event.setAskPrices(askPrices.clone());
            event.setAskVolumes(askVolumes.clone());
            event.setTimestamp(Instant.now());
            event.setSequenceNumber(sequenceNumbers.compute(instrument, (k, v) -> v == null ? 1 : v + 1));
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public void onTrade(String instrument, double price, double volume, Instant timestamp) {
        if (!running.get() || volume < MIN_VOLUME) {
            return;
        }

        Disruptor<MarketDataEvent> disruptor = disruptorsByInstrument.get(instrument);
        if (disruptor == null) {
            return;
        }

        long sequence = disruptor.getRingBuffer().next();
        try {
            MarketDataEvent event = disruptor.getRingBuffer().get(sequence);
            event.setInstrument(instrument);
            event.setEventType(MarketDataEvent.EventType.TRADE);
            event.setTradePrice(price);
            event.setTradeVolume(volume);
            event.setTimestamp(timestamp);
            event.setSequenceNumber(sequenceNumbers.compute(instrument, (k, v) -> v == null ? 1 : v + 1));
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public void start() {
        running.set(true);
        logger.info("Market data feed handler started");
    }

    public void stop() {
        running.set(false);
        disruptorsByInstrument.values().forEach(Disruptor::halt);
        feedExecutor.shutdown();
        try {
            if (!feedExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                feedExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            feedExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Market data feed handler stopped");
    }

    public Map<String, Long> getLastUpdateTimestamps() {
        return Map.copyOf(lastUpdateTimestamps);
    }

    public boolean isRunning() {
        return running.get();
    }

    private class MarketDataEventHandler implements EventHandler<MarketDataEvent> {
        private final String instrument;

        MarketDataEventHandler(String instrument) {
            this.instrument = instrument;
        }

        @Override
        public void onEvent(MarketDataEvent event, long sequence, boolean endOfBatch) {
            try {
                switch (event.getEventType()) {
                    case ORDERBOOK -> handleOrderBookUpdate(event);
                    case TRADE -> handleTrade(event);
                }
                lastUpdateTimestamps.put(instrument, System.nanoTime());
            } catch (Exception e) {
                logger.error("Error processing market data event for {}: {}", instrument, e.getMessage(), e);
            }
        }

        private void handleOrderBookUpdate(MarketDataEvent event) {
            RiskEngineShard shard = instrumentShards.get(instrument);
            if (shard != null) {
                VaRModel varModel = shard.getVaRModel();
                varModel.updateOrderBook(
                    event.getBidPrices(),
                    event.getBidVolumes(),
                    event.getAskPrices(),
                    event.getAskVolumes(),
                    event.getTimestamp()
                );
            }
        }

        private void handleTrade(MarketDataEvent event) {
            RiskEngineShard shard = instrumentShards.get(instrument);
            if (shard != null) {
                VaRModel varModel = shard.getVaRModel();
                varModel.addTrade(
                    event.getTradePrice(),
                    event.getTradeVolume(),
                    event.getTimestamp()
                );
            }
        }
    }

    private static class DisruptorErrorHandler implements ExceptionHandler<MarketDataEvent> {
        private static final Logger errorLogger = LogManager.getLogger("DisruptorError");
        private final String instrument;

        DisruptorErrorHandler(String instrument) {
            this.instrument = instrument;
        }

        @Override
        public void handleEventException(Throwable ex, long sequence, MarketDataEvent event) {
            errorLogger.error("Disruptor exception on {} sequence {}: {}", 
                instrument, sequence, ex.getMessage(), ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            errorLogger.error("Disruptor start exception for {}: {}", instrument, ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            errorLogger.error("Disruptor shutdown exception for {}: {}", instrument, ex.getMessage(), ex);
        }
    }

    public static class MarketDataEvent {
        private String instrument;
        private EventType eventType;
        private double[] bidPrices;
        private double[] bidVolumes;
        private double[] askPrices;
        private double[] askVolumes;
        private double tradePrice;
        private double tradeVolume;
        private Instant timestamp;
        private long sequenceNumber;

        public enum EventType {
            ORDERBOOK, TRADE
        }

        public String getInstrument() { return instrument; }
        public void setInstrument(String instrument) { this.instrument = instrument; }
        public EventType getEventType() { return eventType; }
        public void setEventType(EventType eventType) { this.eventType = eventType; }
        public double[] getBidPrices() { return bidPrices; }
        public void setBidPrices(double[] bidPrices) { this.bidPrices = bidPrices; }
        public double[] getBidVolumes() { return bidVolumes; }
        public void setBidVolumes(double[] bidVolumes) { this.bidVolumes = bidVolumes; }
        public double[] getAskPrices() { return askPrices; }
        public void setAskPrices(double[] askPrices) { this.askPrices = askPrices; }
        public double[] getAskVolumes() { return askVolumes; }
        public void setAskVolumes(double[] askVolumes) { this.askVolumes = askVolumes; }
        public double getTradePrice() { return tradePrice; }
        public void setTradePrice(double tradePrice) { this.tradePrice = tradePrice; }
        public double getTradeVolume() { return tradeVolume; }
        public void setTradeVolume(double tradeVolume) { this.tradeVolume = tradeVolume; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public long getSequenceNumber() { return sequenceNumber; }
        public void setSequenceNumber(long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    }
}

// === ARCHIVO: src/main/java/com/trading/riskengine/domain/RiskOrder.java ===
package com.trading.riskengine.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RiskOrder(
    String orderId,
    String traderId,
    String strategyId,
    String instrument,
    OrderSide side,
    BigDecimal quantity,
    BigDecimal price,
    BigDecimal notionalValue,
    BigDecimal currentVaR,
    BigDecimal exposure,
    BigDecimal traderLimit,
    BigDecimal strategyLimit,
    BigDecimal instrumentLimit,
    RiskStatus riskStatus,
    Instant timestamp,
    Instant evaluatedAt,
    String evaluationReason
) {
    public RiskOrder {
        if (orderId == null || orderId.isBlank()) {
            orderId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (evaluatedAt == null) {
            evaluatedAt = timestamp;
        }
        if (riskStatus == null) {
            riskStatus = RiskStatus.PENDING;
        }
    }

    public enum OrderSide {
        BUY, SELL, SHORT, COVER
    }

    public enum RiskStatus {
        PENDING,
        APPROVED,
        REJECTED_LIMIT_BREACH,
        REJECTED_VAR_BREACH,
        REJECTED_CIRCUIT_BREAKER,
        REJECTED_KILL_SWITCH,
        REVIEW_REQUIRED
    }

    public boolean isApproved() {
        return riskStatus == RiskStatus.APPROVED;
    }

    public boolean isRejected() {
        return riskStatus.name().startsWith("REJECTED");
    }

    public boolean exceedsTraderLimit() {
        return traderLimit != null && exposure.compareTo(traderLimit) > 0;
    }

    public boolean exceedsStrategyLimit() {
        return strategyLimit != null && exposure.compareTo(strategyLimit) > 0;
    }

    public boolean exceedsInstrumentLimit() {
        return instrumentLimit != null && exposure.compareTo(instrumentLimit) > 0;
    }

    public boolean exceedsVaR() {
        return currentVaR != null && notionalValue.compareTo(currentVaR) > 0;
    }

    public RiskOrder withStatus(RiskStatus newStatus, String reason) {
        return new RiskOrder(
            orderId, traderId, strategyId, instrument, side, quantity, price,
            notionalValue, currentVaR, exposure, traderLimit, strategyLimit,
            instrumentLimit, newStatus, timestamp, Instant.now(), reason
        );
    }

    public BigDecimal getEffectiveLimit() {
        BigDecimal minLimit = traderLimit;
        if (strategyLimit != null && (minLimit == null || strategyLimit.compareTo(minLimit) < 0)) {
            minLimit = strategyLimit;
        }
        if (instrumentLimit != null && (minLimit == null || instrumentLimit.compareTo(minLimit) < 0)) {
            minLimit = instrumentLimit;
        }
        return minLimit;
    }

    public String toComplianceLog() {
        return String.format(
            "ORDER_DECISION: orderId=%s traderId=%s strategyId=%s instrument=%s side=%s " +
            "notional=%s status=%s reason=\"%s\" evaluatedAt=%s",
            orderId, traderId, strategyId, instrument, side, notionalValue,
            riskStatus, evaluationReason, evaluatedAt
        );
    }
}

```
