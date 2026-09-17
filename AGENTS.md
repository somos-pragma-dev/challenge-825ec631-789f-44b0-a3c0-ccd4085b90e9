# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Implementación de un motor de riesgo en tiempo real con circuit breakers**.

| | |
|---|---|
| Tema | motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos |
| Nivel | master-l2 |
| Chapter | Generico |
| Especialidad | Inferido del contexto |
| Stack | Java 21 / LMAX Disruptor 5.2 |
| Patron arquitectonico | reactive ring-buffer con sharding por instrumento + consensus eventual para metadata global |
| Tiempo estimado | 4 semanas |

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `el comando de build o arranque canonico del stack elegido` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `el comando de build o arranque canonico del stack elegido` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Evaluación de riesgos y límites en tiempo real**: Mecanismo para evaluar riesgos y aplicar límites en tiempo real.
- **Fase 2 — Justificación de la elección de lenguaje y estructuras de datos**: Documento que justifica la elección de lenguaje y estructuras de datos.
- **Fase 3 — Garantizar consistencia entre motores de riesgo en paralelo**: Mecanismo para garantizar la consistencia entre motores de riesgo en paralelo.
- **Fase 4 — Política de kill switch y estrategia de replay determinístico**: Política de kill switch y estrategia de replay determinístico implementadas.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Lo que falta y tenes que completar

### 1. Referencias colgando (63)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `OrderSide`
      OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.domain.OrderSide.
- [ ] `src/test/java/com/trading/riskengine/RiskEngineShardTest.java` — `OrderSide`
      OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.domain.OrderSide.
- [ ] `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchStatus`
      KillSwitchStatus se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.interfaces.KillSwitchStatus (hay mas de un tipo con ese nombre en el proyecto).
- [ ] `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `OrderSide`
      OrderSide se usa en el cuerpo del archivo pero no esta importado. El proyecto lo declara en com.trading.riskengine.domain.OrderSide.
- [ ] `src/main/java/com/trading/riskengine/Main.java` — `MarketDataFeedHandler`
      El import com.trading.riskengine.infrastructure.MarketDataFeedHandler no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/main/java/com/trading/riskengine/interfaces/RiskEngineQueryService.java` — `VaRModel`
      El import com.trading.riskengine.domain.VaRModel no se usa en ningun lado del cuerpo del archivo. Se puede eliminar.
- [ ] `src/main/java/com/trading/riskengine/Main.java` — `KillSwitchPolicy.isTradingHalted`
      Se invoca `isTradingHalted` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.isAccepted`
      Se invoca `isAccepted` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.riskScore`
      Se invoca `riskScore` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.currentVaR`
      Se invoca `currentVaR` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/Main.java` — `RiskEvaluationResult.rejectionReason`
      Se invoca `rejectionReason` sobre `RiskEvaluationResult`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/domain/RiskOrder.java` — `RiskStatus.name`
      Se invoca `name` sobre `RiskStatus`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setInstrument`
      Se invoca `setInstrument` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setEventType`
      Se invoca `setEventType` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setBidPrices`
      Se invoca `setBidPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setBidVolumes`
      Se invoca `setBidVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setAskPrices`
      Se invoca `setAskPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setAskVolumes`
      Se invoca `setAskVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setTimestamp`
      Se invoca `setTimestamp` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setSequenceNumber`
      Se invoca `setSequenceNumber` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setTradePrice`
      Se invoca `setTradePrice` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.setTradeVolume`
      Se invoca `setTradeVolume` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getEventType`
      Se invoca `getEventType` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getBidPrices`
      Se invoca `getBidPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getBidVolumes`
      Se invoca `getBidVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getAskPrices`
      Se invoca `getAskPrices` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getAskVolumes`
      Se invoca `getAskVolumes` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getTimestamp`
      Se invoca `getTimestamp` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getTradePrice`
      Se invoca `getTradePrice` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java` — `MarketDataEvent.getTradeVolume`
      Se invoca `getTradeVolume` sobre `MarketDataEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskOrder.quantity`
      Se invoca `quantity` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskOrder.price`
      Se invoca `price` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskOrder.orderId`
      Se invoca `orderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskEvent.setOrder`
      Se invoca `setOrder` sobre `RiskEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskEvent.setSubmitTime`
      Se invoca `setSubmitTime` sobre `RiskEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/RiskEngineShard.java` — `RiskEvent.getOrder`
      Se invoca `getOrder` sobre `RiskEvent`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.orderId`
      Se invoca `orderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.strategyId`
      Se invoca `strategyId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getRecentVolatility`
      Se invoca `getRecentVolatility` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.isAnomalous`
      Se invoca `isAnomalous` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.recordOrder`
      Se invoca `recordOrder` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.notional`
      Se invoca `notional` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getStandardDeviation`
      Se invoca `getStandardDeviation` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.traderId`
      Se invoca `traderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getMeanOrderSize`
      Se invoca `getMeanOrderSize` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getOrdersPerSecond`
      Se invoca `getOrdersPerSecond` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getExpectedOrdersPerSecond`
      Se invoca `getExpectedOrdersPerSecond` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getBuyCount`
      Se invoca `getBuyCount` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `StrategyAnomalyTracker.getSellCount`
      Se invoca `getSellCount` sobre `StrategyAnomalyTracker`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.combinedScore`
      Se invoca `combinedScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.zScore`
      Se invoca `zScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.frequencyScore`
      Se invoca `frequencyScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `AnomalyScore.sideImbalanceScore`
      Se invoca `sideImbalanceScore` sobre `AnomalyScore`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java` — `RiskOrder.side`
      Se invoca `side` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.instrument`
      Se invoca `instrument` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.var`
      Se invoca `var` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.volatility`
      Se invoca `volatility` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/VaRModelTest.java` — `VaRSnapshot.timestamp`
      Se invoca `timestamp` sobre `VaRSnapshot`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/RiskEngineShardTest.java` — `RiskOrder.orderId`
      Se invoca `orderId` sobre `RiskOrder`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchPolicy.evaluate`
      Se invoca `evaluate` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchStatus.triggered`
      Se invoca `triggered` sobre `KillSwitchStatus`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchPolicy.getHistory`
      Se invoca `getHistory` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java` — `KillSwitchPolicy.reset`
      Se invoca `reset` sobre `KillSwitchPolicy`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (14)

- `pom.xml`
- `src/main/java/com/trading/riskengine/Main.java`
- `src/main/resources/application.properties`
- `src/main/java/com/trading/riskengine/domain/RiskOrder.java`
- `src/main/java/com/trading/riskengine/domain/VaRModel.java`
- `src/main/java/com/trading/riskengine/interfaces/RiskEngineQueryService.java`
- `src/main/java/com/trading/riskengine/infrastructure/MarketDataFeedHandler.java`
- `src/main/java/com/trading/riskengine/application/RiskEngineShard.java`
- `src/main/java/com/trading/riskengine/config/ResilienceConfig.java`
- `src/main/java/com/trading/riskengine/application/KillSwitchPolicy.java`
- `src/test/java/com/trading/riskengine/VaRModelTest.java`
- `src/test/java/com/trading/riskengine/RiskEngineShardTest.java`
- `src/test/java/com/trading/riskengine/KillSwitchPolicyTest.java`
- `scripts/replay/deterministic_replay.sh`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/trading/riskengine`
- `src/main/java/com/trading/riskengine/domain`
- `src/main/java/com/trading/riskengine/infrastructure`
- `src/main/java/com/trading/riskengine/application`
- `src/main/java/com/trading/riskengine/config`
- `src/main/java/com/trading/riskengine/interfaces`
- `src/test/java/com/trading/riskengine`
- `src/main/resources`
- `scripts/replay`

## Verificacion

```bash
el comando de build o arranque canonico del stack elegido
```

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **reactive ring-buffer con sharding por instrumento + consensus eventual para metadata global**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Brecha que el reto ataca: Sistema master-l2 que evalúa el riesgo de cada orden antes de enviarla al exchange, en menos de 500 microsegundos p99. Consume feed de market data (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera thresholds calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples risk engines corriendo en paralelo (consensus vs sharding por instrument), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidents. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
