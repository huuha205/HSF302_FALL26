# Student Management System (JavaFX + Spring Data JPA)

## Yêu cầu môi trường
- JDK 21, Maven 3.9+
- SQL Server (TCP/IP port 1433 bật, tài khoản `sa`)
- IntelliJ IDEA, Gluon Scene Builder (tuỳ chọn)

## Cài đặt
1. Tạo database trong SQL Server:
   ```sql
   IF DB_ID('StudentDB') IS NULL CREATE DATABASE StudentDB;
   ```
2. Cấu hình thông tin kết nối trong `ui-module/src/main/resources/application.properties`:
   - `spring.datasource.username=sa`
   - `spring.datasource.password=sa`

## Build & Test
```bash
mvn clean install
```
Bộ kiểm thử dùng H2 in-memory database, chạy độc lập không cần SQL Server:
- `StudentRepositoryTest`: 10/10 test cases pass.
- `StudentServiceImplTest`: 16/16 test cases pass (12 test methods).
- Tổng cộng: **26/26 test cases PASS**.

## Chạy ứng dụng
- **IntelliJ IDEA:** Chuột phải vào `com.example.ui.Launcher` -> chọn **Run 'Launcher.main()'**.
- **Terminal:**
  ```bash
  mvn -q clean install -DskipTests
  mvn -f ui-module/pom.xml spring-boot:run
  ```

## Kiến trúc N-Layer
`StudentController` → `StudentService` (interface) → `StudentServiceImpl` → `StudentRepository` (`JpaRepository`) → SQL Server

| Module | Nội dung |
|---|---|
| **data-module** | Entity `Student`, Repository (Spring Data JPA), Service (`StudentService`) + Impl (`StudentServiceImpl`), Exceptions |
| **ui-module** | JavaFX (FXML, CSS), `StudentController` (@Component), cấu hình Spring Boot Bootstrap |

## Danh sách Commit Git chuẩn
1. `chore: init multi-module spring boot project for JDK 21`
2. `feat(data): add Student entity`
3. `feat(data): add StudentRepository extending JpaRepository`
4. `feat(data): add StudentService and StudentServiceImpl`
5. `feat(ui): bootstrap Spring context in JavaFX app`
6. `feat(ui): load, search and select students`
7. `feat(ui): add, update, delete and clear student`
8. `style(ui): add stylesheet and polish UX`
9. `docs: add README with setup and run guide`

## Các tính năng Nâng cao (Bài 8 - Bonus)
- **8.2 Async loading với Task (+3đ):** Thực hiện tải dữ liệu và tìm kiếm trên background daemon thread thông qua `javafx.concurrent.Task`, hiển thị trạng thái `Loading...` và disable ô search trong lúc tải, cập nhật UI mượt mà không bị giật lag.
  - Nhánh: `feature/async-loading`
- **8.3 Export CSV (+2đ):** Bổ sung nút **Export CSV** mở `FileChooser` cho phép người dùng chọn nơi lưu file `.csv`. Hỗ trợ mã hóa UTF-8 BOM (`\uFEFF`) giúp Excel mở file tiếng Việt đúng dấu và escape dấu phẩy `,` cùng dấu ngoặc kép `"`.
  - Nhánh: `feature/export-csv`
