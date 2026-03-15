# Product Catalog

## Requirements

* **Java 21** (OpenJDK or any distribution)
* **Maven** (bundled with `mvnw` wrapper, optional)
* **PostgreSQL 16** (or compatible version)
* **Optional:** Docker for quick Postgres setup

---

## 1. PostgreSQL Setup

### Option A: Using Docker (recommended)

Run the following command to start Postgres with required credentials:

```bash
docker run -d \
  -p 5432:5432 \
  -e POSTGRES_DB=product_catalog \
  -e POSTGRES_USER=product_catalog \
  -e POSTGRES_PASSWORD=product_catalog \
  postgres:16

```

* **POSTGRES_DB** → database name (`product_catalog`)
* **POSTGRES_USER** → username (`product_catalog`)
* **POSTGRES_PASSWORD** → password (`product_catalog`)
* **The container exposes Postgres on:** `localhost:5432`

### Option B: Manual Postgres Installation

#### Linux / Ubuntu

```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo -u postgres psql

```

**Inside psql:**

```sql
CREATE DATABASE product_catalog;
CREATE USER product_catalog WITH PASSWORD 'product_catalog';
GRANT ALL PRIVILEGES ON DATABASE product_catalog TO product_catalog;
\q

```

#### Windows

1. **Download and install** PostgreSQL 16 from [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)
2. **During setup**, create a database: `product_catalog`
3. **Create a user**: `product_catalog` with password: `product_catalog`
4. **Ensure port 5432** is open

#### MacOS

```bash
brew install postgresql
brew services start postgresql
psql postgres

```

**Then inside psql:**

```sql
CREATE DATABASE product_catalog;
CREATE USER product_catalog WITH PASSWORD 'product_catalog';
GRANT ALL PRIVILEGES ON DATABASE product_catalog TO product_catalog;
\q

```

> **Note:** These credentials match the `application.yaml` configuration.

---

## 2. Running the Application

**Using Maven Wrapper:**

```bash
./mvnw spring-boot:run

```

**Or with Maven:**

```bash
mvn spring-boot:run

```

**The application will start on:** [http://localhost:8080](https://www.google.com/search?q=http://localhost:8080)

---

## 3. Swagger UI (API Documentation & DX)

For better developer experience, the project exposes Swagger UI:

* [http://localhost:8080/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui/index.html)
* **Allows testing** endpoints interactively
* **Documents** filters, pagination, and DTOs


---

## 4. Design Decisions (Highlights)

* **PostgreSQL over H2 or Oracle**
* The `attributes` column of `Product` **Varies** a lot so that it is not right to use strict schema.
* **JSONB** allows storing dynamic attributes (50–200 varying fields per product).
* Utilized **GIN index** support for performant querying of JSONB fields.
* Flexible schema helps handle future product attribute growth.


* **Dynamic filtering via JPA Specification + CriteriaBuilder**
* Allows searching by **name**, **producer**, or any **JSONB attribute**.
* **Pagination support** using Spring Data `Pageable`.
* **Fetch joins** used carefully to avoid N+1 issues.


* **Transaction management and dirty checking**
* Service layer uses `@Transactional`.
* Avoids unnecessary repository `save()` calls for updates leaving the job for dirty checking.


* **Swagger integration**
* Improves **DX** (developer experience).
* Makes API **self-documenting**.



