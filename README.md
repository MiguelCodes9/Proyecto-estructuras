# FUT-sala
El proyecto de estructuras de datos esta orientado a la renta o planificacion de partidos de futbol. Como tal, este programa hace permite a jugadores reservar canchas de fútbol y a un administrador
gestionar las solicitudes.
Los integrantes del proyecto son:
* Juliana Bastidas
* Miguel Angel Moreno
* Edwin Susatama


**Estructura del Proyecto**

    canchas/
    └── src/com/canchas/
        ├── structures/
        │   ├── Nodo.java
        │   ├── ListaEnlazada.java
        │   ├── EntradaHash.java
        │   ├── HashTable.java
        │   ├── TreeNode.java
        │   ├── BinarySearchTree.java
        │   └── Graph.java
        ├── models/
        │   ├── User.java
        │   ├── Field.java
        │   └── Reservation.java
        ├── services/
        │   ├── AuthService.java
        │   ├── FieldService.java
        │   └── ReservationService.java
        └── Main.java

---

**Estructuras de Datos Implementadas**

**ListaEnlazada**

Lista simplemente enlazada genérica con punteros head y tail.
Operaciones principales: addFirst, addLast y removeFirst en O(1). Search y remove en O(n).

Usos en el sistema:
- Cola FIFO de solicitudes de reserva pendientes en ReservationService
- Historial de reservas por usuario
- Encadenamiento de colisiones dentro de la HashTable
- Almacenamiento de canchas por nodo dentro del BinarySearchTree

---

**HashTable**

Tabla hash con encadenamiento separado. Internamente es un arreglo de ListaEnlazada,
lo que la convierte en un arreglo de listas enlazadas. La capacidad es 97 (número primo
para reducir colisiones). La función hash es abs(key.hashCode()) % capacidad.

Usos en el sistema:
- AuthService almacena usuarios indexados por username con acceso en O(1) promedio
- ReservationService indexa el historial de cada jugador por username

---

**BinarySearchTree y TreeNode**

Árbol Binario de Búsqueda genérico implementado desde cero. Cada nodo guarda una clave
entera (key), un dato genérico (data), y referencias al hijo izquierdo y derecho.
Cada nodo almacena una ListaEnlazada de canchas para soportar múltiples registros
con la misma clave sin pérdida de datos.

Operaciones implementadas:
- insertar: O(log n) promedio
- buscar: O(log n) promedio
- eliminar con sucesor in-order: O(log n) promedio
- recorridoInOrder: devuelve los datos en orden ascendente
- recorridoPreOrder: refleja la estructura interna del árbol
- buscarPorRango(min, max): filtra por rango de clave con poda de ramas

Usos en el sistema:
- FieldService mantiene dos BST: uno ordenado por capacidad de jugadores (5, 7 u 11)
  y otro por precio por hora. Permite listar y filtrar canchas por rango en O(log n).

---

**Graph**

Grafo ponderado no dirigido representado como matriz de adyacencia double[][].
Implementa el algoritmo de Dijkstra para encontrar el camino más corto entre dos sedes.

Usos en el sistema:
- 19 localidades de Bogotá como vértices: 5 sedes principales con cancha y 14 de paso
- Al reservar, el jugador puede consultar la ruta más corta desde su localidad hasta la sede

---

**Flujo del Sistema**

El sistema tiene tres roles de acceso: jugador, administrador y consulta de rutas.

Panel del jugador:
- Ver catálogo de canchas ordenado por capacidad o precio (In-Order del BST)
- Filtrar canchas por rango de capacidad o precio (buscarPorRango del BST)
- Solicitar reserva de cancha
- Calcular ruta más corta hacia una sede (Dijkstra)
- Consultar historial de reservas propias

Panel del administrador:
- Registrar nuevas canchas en el catálogo (inserción en el BST)
- Ver la cola de solicitudes pendientes (ListaEnlazada FIFO)
- Aprobar o rechazar la siguiente solicitud en la cola
- Ver la estructura interna del árbol de canchas

**Cómo Ejecutar**

Requisitos: Java 8 o superior.

Ejecutura Main.java

Credenciales de prueba:

| Rol | Usuario | Contraseña |
|---|---|---|
| Jugador | migue | 123 |
| Jugador | juli | 456 |
| Administrador | admin | admin123 |

---

**Sedes Disponibles**

| Sede | Cancha | Modalidad | Precio por hora |
|---|---|---|---|
| Suba | El Bosque | Fútbol 7 | $60.000 |
| Engativá | La 80 | Fútbol 5 | $45.000 |
| Teusaquillo | Estadio | Fútbol 11 | $90.000 |
| Usaquén | Los Pinos | Fútbol 5 | $40.000 |
| Kennedy | Sur Norte | Fútbol 7 | $55.000 |


