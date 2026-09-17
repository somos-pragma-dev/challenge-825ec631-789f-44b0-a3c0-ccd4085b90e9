# Implementación de un motor de riesgo en tiempo real con circuit breakers

El sistema debe evaluar el riesgo de cada orden de trading antes de enviarla al exchange, en menos de 500 microsegundos en el percentil 99. Consume un feed de datos de mercado (nivel 2 orderbook + trades), mantiene un modelo de VaR intraday, aplica límites por trader/estrategia/instrumento en tiempo real, y dispara circuit breakers dinámicos que bloquean nuevas órdenes cuando la exposición supera umbrales calibrados por volatilidad. Debe justificar el uso de estructuras lock-free vs mutex, la elección entre C++ vs Rust vs Java LMAX Disruptor, cómo garantiza la consistencia entre múltiples motores de riesgo corriendo en paralelo (consensus vs sharding por instrumento), la política de kill switch cuando detecta un algoritmo que se comporta de forma anómala, y la estrategia de replay determinístico para post-mortem de incidentes. Incluye compliance con regulaciones MiFID II para trazabilidad de decisiones de riesgo.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | motor de risk scoring en tiempo real para trading algorítmico con circuit breakers dinámicos |
| **Nivel** | master-l2 |
| **Tipo** | mixed |
| **Tiempo estimado** | 4 semanas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Evaluación de riesgos y límites en tiempo real

**Objetivo:** Implementar la funcionalidad para evaluar el riesgo de cada orden antes de enviarla al exchange, aplicando límites por trader/estrategia/instrumento.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Diseñar el mecanismo para consumir el feed de datos de mercado y mantener un modelo de VaR intraday.
- Implementar la lógica para aplicar límites en tiempo real y disparar circuit breakers cuando la exposición supera los umbrales.

**Entregable:** Mecanismo para evaluar riesgos y aplicar límites en tiempo real.

<details>
<summary>Pistas de conocimiento</summary>

- Considerar la latencia y el throughput requeridos para cumplir con los tiempos de respuesta.
- Evaluar la consistencia de los datos al aplicar límites y disparar circuit breakers.

</details>

### Fase 2: Justificación de la elección de lenguaje y estructuras de datos

**Objetivo:** Justificar el uso de estructuras lock-free vs mutex y la elección entre C++ vs Rust vs Java LMAX Disruptor.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Analizar las ventajas y desventajas de cada opción y justificar la elección basada en los requisitos del sistema.
- Considerar la latencia, el throughput y la consistencia al tomar la decisión.

**Entregable:** Documento que justifica la elección de lenguaje y estructuras de datos.

<details>
<summary>Pistas de conocimiento</summary>

- Evaluar el rendimiento y la escalabilidad de cada opción.
- Considerar la facilidad de mantenimiento y la curva de aprendizaje para el equipo de desarrollo.

</details>

### Fase 3: Garantizar consistencia entre motores de riesgo en paralelo

**Objetivo:** Implementar la lógica para garantizar la consistencia entre múltiples motores de riesgo corriendo en paralelo.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Evaluar las opciones de consensus vs sharding por instrumento y justificar la elección basada en los requisitos del sistema.
- Implementar la lógica para garantizar la consistencia entre los motores de riesgo.

**Entregable:** Mecanismo para garantizar la consistencia entre motores de riesgo en paralelo.

<details>
<summary>Pistas de conocimiento</summary>

- Considerar la latencia y el throughput requeridos para cumplir con los tiempos de respuesta.
- Evaluar la complejidad de implementación y mantenimiento de cada opción.

</details>

### Fase 4: Política de kill switch y estrategia de replay determinístico

**Objetivo:** Implementar la política de kill switch y la estrategia de replay determinístico para post-mortem de incidentes.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Diseñar la política de kill switch para detectar y bloquear algoritmos que se comporten de forma anómala.
- Implementar la estrategia de replay determinístico para analizar incidentes y realizar post-mortem.

**Entregable:** Política de kill switch y estrategia de replay determinístico implementadas.

<details>
<summary>Pistas de conocimiento</summary>

- Considerar la latencia y el throughput requeridos para cumplir con los tiempos de respuesta.
- Evaluar la efectividad de la política de kill switch y la estrategia de replay determinístico.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es un motor de riesgo en tiempo real y cuáles son sus componentes principales?
- **paraQueSirve**: ¿Para qué sirve un motor de riesgo en tiempo real en el contexto del trading algorítmico?
- **comoSeUsa**: ¿Cómo se utiliza un motor de riesgo en tiempo real para evaluar el riesgo de cada orden antes de enviarla al exchange?
- **erroresComunes**: ¿Cuáles son los errores comunes al implementar un motor de riesgo en tiempo real y cómo se pueden evitar?
- **queDecisionesImplica**: ¿Qué decisiones implica la implementación de un motor de riesgo en tiempo real con circuit breakers dinámicos?

## Criterios de Evaluacion

- Implementación de la funcionalidad para evaluar el riesgo de cada orden antes de enviarla al exchange.
- Justificación del uso de estructuras lock-free vs mutex y la elección entre C++ vs Rust vs Java LMAX Disruptor.
- Implementación de la lógica para garantizar la consistencia entre múltiples motores de riesgo corriendo en paralelo.
- Implementación de la política de kill switch y la estrategia de replay determinístico para post-mortem de incidentes.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
el comando de build o arranque canonico del stack elegido
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
