package click.escuela.school.admin.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.model.Excel;

public interface ExcelRepository extends JpaRepository<Excel, UUID>{

}
