JDBC PostgreSQL – Projet Hady Souaiby

Projet Java démontrant la connexion à une base de données **PostgreSQL** et l’utilisation des opérations **CRUD** (Create, Read, Update, Delete) via **JDBC**.

Prérequis et versions

1- Java 17

2- Maven

3- PostgreSQL installé et en cours d’exécution sur la machine

Connexion PostgreSQL

-Paramètres de connexion

La connexion est définie dans la classe `App.java` :

1-URL: `jdbc:postgresql://localhost:5432/jdbc_hady` | Adresse du serveur et nom de la base

2-User: `postgres` | Utilisateur PostgreSQL

3-Password:  `123` | Mot de passe de l’utilisateur

4-Host: `localhost`

5-Port : `5432` (port par défaut de PostgreSQL)

6-Base de données: `jdbc_hady`

-Établissement de la connexion

La connexion est obtenue avec `DriverManager.getConnection()` :

```java
Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
```

Le pilote JDBC PostgreSQL est fourni par la dépendance Maven `org.postgresql:postgresql` (version 42.7.3) dans le `pom.xml` (ajout des dependencies)


Au prélable, on a  crée la base et l’utilisateur pour dans pgAdmin4 (et non psql pour GUI).

```sql
CREATE DATABASE jdbc_hady;
```

Table utilisée : `students`

Structure de la table
| Colonne | Type | Description |

|---------|------|-------------|

| **id** | `SERIAL` (entier auto-incrémenté) | Identifiant unique de l’étudiant |

| **name** | `VARCHAR` | Nom de l’étudiant |

| **email** | `VARCHAR` | Adresse e-mail |

Donc pour créer cette table:

```sql
CREATE TABLE students (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);
```

Méthodes CRUD

Toutes les opérations sont implémentées dans `App.java`.

1-Create (INSERT) – `insertStudent`

Rôle: insérer un nouvel étudiant et récupérer son `id` généré.

- **Signature** : `insertStudent(Connection conn, String name, String email)`

- **Requête** : `INSERT INTO students(name, email) VALUES (?, ?) RETURNING id`

- **Technique** : `PreparedStatement` pour éviter les injections SQL et `RETURNING id` pour récupérer l’id créé.

- **Retour** : l’`id` (int) du nouvel enregistrement.

Exemple dans le `main` :

```java
int newId = insertStudent(conn, "Student One", "student1@mail.com");
```

2-Read (SELECT) – `selectAllStudents`

Rôle: afficher tous les étudiants de la table.

- **Signature** : `selectAllStudents(Connection conn)`

- **Requête** : `SELECT id, name, email FROM students ORDER BY id`

- **Technique** : `Statement` + `ResultSet`, parcours avec `while (rs.next())`.

- **Retour** : aucun ; les lignes sont affichées dans la console.

Exemple dans le `main`:
```java
selectAllStudents(conn);
```

3- Update – `updateStudentName`

Rôle: modifier le nom d’un étudiant à partir de son `id`.

- **Signature** : `updateStudentName(Connection conn, int id, String newName)`

- **Requête** : `UPDATE students SET name=? WHERE id=?`

- **Technique** : `PreparedStatement` avec paramètres pour l’id et le nouveau nom.

- **Retour** : nombre de lignes mises à jour (int).

Exemple dans le `main` :
```java
int updated = updateStudentName(conn, newId, "Student One Updated");
```

4-Delete – `deleteStudent`

Rôle: supprimer un étudiant à partir de son `id`.

- **Signature** : `deleteStudent(Connection conn, int id)`

- **Requête** : `DELETE FROM students WHERE id=?`

- **Technique** : `PreparedStatement` avec l’id en paramètre.

- **Retour** : nombre de lignes supprimées (int).

Exemple dans le `main` :
```java
int deleted = deleteStudent(conn, newId);
System.out.println("Deleted rows = " + deleted);
```

Lancer le projet

À la racine du projet :

```bash
mvn compile exec:java -Dexec.mainClass="org.example.App"
```
Ou ouvrir le projet dans un IDE (ex. IntelliJ IDEA) et exécuter la classe `App`.
Personnellement, j'ai utilisé IntelliJ IDEA.

Les screenshots et plus d'explications sont tous dans le pdf.
