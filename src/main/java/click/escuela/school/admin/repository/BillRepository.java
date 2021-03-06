package click.escuela.school.admin.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.model.Bill;

public interface BillRepository extends JpaRepository<Bill, UUID> {

	Optional<Bill> findByIdAndSchoolId(UUID id, UUID schoolId);

}
