package click.escuela.school.admin.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.model.Bill;

public interface BillRepository extends JpaRepository<Bill, UUID> {

	public List<Bill> findByStudentIdAndMonthAndYearAndStatus(UUID studentId, Integer month, Integer year,
			PaymentStatus status);

	public List<Bill> findByStudentIdAndMonthAndYear(UUID studentId, Integer month, Integer year);

	public List<Bill> findByStudentIdAndMonthAndStatus(UUID studentId, Integer month, PaymentStatus status);

	public List<Bill> findByStudentIdAndYearAndStatus(UUID studentId, Integer year, PaymentStatus status);

	public List<Bill> findByStudentIdAndYear(UUID studentId, Integer year);

	public List<Bill> findByStudentIdAndMonth(UUID studentId, Integer month);

	public List<Bill> findByStudentIdAndStatus(UUID studentId, PaymentStatus status);
}
