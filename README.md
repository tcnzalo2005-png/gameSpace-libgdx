# Space Navigation

Videojuego de estilo *Space Invaders* desarrollado en Java utilizando el framework **libGDX**. El jugador controla una nave espacial, combate contra distintos tipos de enemigos, recoge mejoras y avanza por rondas mientras intenta superar su puntuación máxima.

# Autores 
- **Nicolas Castellano**
- **Nicolas Caniguante**
- **Bejamin chonca**
- **Lucas Salamanca** 
## Características

- **Combate espacial:** movimiento de la nave, disparos y colisiones con enemigos y proyectiles.
- **Enemigos variados:** incluye Tanques, con movimiento lineal, y Tiradores, con movimiento ondulante y disparos periódicos.
- **Sistema de proyectiles:** permite configurar independientemente el movimiento y el comportamiento de las balas al impactar.
- **Power-ups:** aparecen periódicamente y permiten recuperar vidas, aumentar el daño o utilizar balas perforantes temporalmente.
- **Progresión por rondas:** al eliminar a todos los enemigos, aumenta la ronda, se incrementan las velocidades y aparecen más enemigos.
- **Sistema narrativo:** muestra mensajes asociados a determinados intervalos de puntuación.
- **Puntuación y récord:** se obtiene puntuación al eliminar enemigos y se mantiene un récord durante la ejecución de la aplicación.

## Tecnologías utilizadas

- **Java:** lenguaje de programación.
- **libGDX:** framework para el desarrollo del videojuego.
- **LWJGL3:** backend utilizado para ejecutar el juego en escritorio.
- **Gradle:** sistema de construcción habitual en la estructura del proyecto libGDX.

## Cómo ejecutar el proyecto

1. Clona o descarga el repositorio.
2. Importa el proyecto en un IDE compatible con Java y Gradle.
3. Comprueba que las dependencias de libGDX estén configuradas correctamente.
4. Ejecuta la clase `DesktopLauncher`, situada en el módulo de escritorio.

La clase `DesktopLauncher` configura el título de la ventana y una frecuencia objetivo de 60 FPS antes de iniciar el juego.

## Controles

| Acción | Control |
|---|---|
| Mover la nave | Teclas de dirección |
| Disparar | Barra espaciadora |
| Comenzar la partida | Clic o cualquier tecla en el menú |
| Reiniciar tras una derrota | Clic o cualquier tecla en Game Over |

## Mecánicas del juego

### Enemigos

El juego dispone de dos tipos principales de enemigos:

- **Tanque:** tiene 10 vidas y se desplaza siguiendo un movimiento lineal.
- **Tirador:** tiene 3 vidas, se desplaza con una variación ondulante y dispara a intervalos variables.

Ambos heredan de la clase abstracta `Enemigos`, que centraliza funcionalidades compartidas como el movimiento, el rebote en los bordes, las colisiones y la recepción de daño.

### Proyectiles

Las balas utilizan dos estrategias independientes:

- **Estrategia de movimiento:** determina cómo se desplaza el proyectil.
- **Estrategia de impacto:** determina si la bala desaparece al impactar o continúa atravesando enemigos.

El impacto normal destruye la bala al colisionar, mientras que el impacto perforante permite que siga su trayectoria y alcance a otros enemigos.

### Power-ups

Las mejoras aparecen aleatoriamente durante la partida y aplican uno de los siguientes efectos:

| Power-up | Efecto |
|---|---|
| Curación | Recupera una vida. |
| Daño | Aumenta el daño de la nave en dos unidades. |
| Bala perforante | Permite disparar balas perforantes durante 20 segundos. |

Los efectos se implementan mediante una interfaz común, lo que permite agregar nuevos tipos de mejoras sin modificar directamente la lógica de aplicación de cada efecto.

### Puntuación y narrativa

Eliminar un enemigo otorga 10 puntos. La clase `ControladorNarrativa` selecciona mensajes de la historia según la puntuación alcanzada, utilizando objetos `Narrativa` que definen un mensaje y su intervalo correspondiente.

Al eliminar a todos los enemigos se supera la ronda, aumenta la dificultad y se conserva el progreso que se pasa a la siguiente pantalla de juego.

## Arquitectura del proyecto

Las clases se organizan según sus responsabilidades principales.

| Clase o grupo | Responsabilidad |
|---|---|
| `DesktopLauncher` | Configura e inicia la aplicación de escritorio. |
| `SpaceNavigation` | Administra los recursos compartidos, el récord y la pantalla activa. |
| `PantallaMenu` | Muestra el menú inicial y permite comenzar una partida. |
| `PantallaJuego` | Coordina la actualización, el renderizado y las transiciones de la partida. |
| `PantallaGameOver` | Muestra la derrota y permite reiniciar. |
| `MundoJuego` | Administra el estado de la partida, enemigos, proyectiles, colisiones, puntuación y power-ups. |
| `Nave4` | Representa la nave del jugador y gestiona sus controles, vidas y disparos. |
| `Enemigos`, `Tanque`, `Tirador` | Definen el comportamiento común y las variantes de los enemigos. |
| `Bullet` y estrategias | Implementan los proyectiles y sus comportamientos configurables. |
| `PowerUp` y efectos | Representan las mejoras y aplican sus efectos a la nave. |
| `NaveBuilder` y `NaveBasicaBuilder` | Permiten construir la nave mediante una secuencia de configuración. |
| `ControladorNarrativa` y `Narrativa` | Gestionan los mensajes narrativos asociados a la puntuación. |

## Patrones de diseño

El proyecto utiliza cuatro patrones de diseño para organizar y separar comportamientos.

### Singleton

**Clase:** `SpaceNavigation`.

Mantiene una referencia estática a la instancia principal de la aplicación y proporciona acceso mediante `getInstance()`. Las demás clases utilizan este acceso para obtener recursos compartidos, consultar el récord o cambiar la pantalla activa.

### Builder

**Clases:** `NaveBuilder` y `NaveBasicaBuilder`.

Permite construir una nave configurando sus características mediante distintos métodos, como la posición, el sprite, los sonidos, las vidas y la velocidad. Finalmente, `build()` devuelve la instancia de `Nave4` construida.

### Strategy

**Clases principales:** `EstrategiaMovimientoBala`, `EstrategiaImpactoBala` y sus implementaciones.

Permite cambiar el comportamiento de los proyectiles sin modificar directamente la clase `Bullet`. Las estrategias de movimiento e impacto son independientes, lo que facilita combinar diferentes comportamientos.

Los efectos de los power-ups también utilizan una interfaz común, permitiendo aplicar mejoras mediante implementaciones específicas.

### Template Method

**Clases:** `Enemigos`, `Tanque` y `Tirador`.

La clase abstracta `Enemigos` define una secuencia común de actualización mediante el método final `update()`. Las subclases implementan el movimiento particular de cada enemigo y pueden especializar otros comportamientos, como la decisión de disparar.

Este diseño reutiliza la lógica compartida y permite añadir nuevos tipos de enemigos con comportamientos diferentes.

## Recursos del juego

El proyecto utiliza recursos gráficos y de audio para representar la nave, los enemigos, los proyectiles y los power-ups, además de música de fondo y efectos de sonido.

Las texturas se almacenan en un `HashMap`, asociadas a identificadores como `Nave`, `Tanque`, `Tirador`, `Bala`, `Curacion`, `Danio` y `BalaPerforante`.

## Posibles mejoras

- Mejorar la gestión y liberación de los recursos gráficos y de audio.
- Unificar la actualización de posiciones utilizando el tiempo transcurrido (`delta`).
- Persistir el récord entre ejecuciones.
- Ampliar la variedad de enemigos, proyectiles y power-ups.
- Mejorar la separación de responsabilidades en las clases que concentran varias tareas.
- Añadir nuevas pantallas y elementos de interfaz.

## Autoría

Proyecto académico desarrollado en Java con libGDX.
