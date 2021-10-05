package click.escuela.school.admin.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.api.BillStatusApi;
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.enumerator.BillEnum;
import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.enumerator.StudentMessage;
import click.escuela.school.admin.exception.BillException;
import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.model.Student;
import click.escuela.school.admin.repository.BillRepository;
import click.escuela.school.admin.service.BillServiceGeneric;

@Service
public class BillServiceImpl implements BillServiceGeneric<BillApi, BillDTO> {

	@Autowired
	private BillRepository billRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private StudentServiceImpl studentService;

	@Override
	public void create(String schoolId, String id, BillApi billApi) throws BillException {
		try {
			Bill bill = Mapper.mapperToBill(billApi);
			bill.setSchoolId(UUID.fromString(schoolId));
			bill.setStatus(PaymentStatus.PENDING);
			Student student = studentService.findByIdAndSchoolId(schoolId, id)
					.orElseThrow(() -> new StudentException(StudentMessage.GET_ERROR));
			bill.setStudent(student);
			billRepository.save(bill);
		} catch (Exception e) {
			throw new BillException(BillEnum.CREATE_ERROR);
		}
	}

	@Override
	public BillDTO getById(String billId, String schoolId) throws BillException {
		return Mapper.mapperToBillDTO(findById(billId, schoolId));
	}

	@Override
	public List<BillDTO> findAll() {
		return Mapper.mapperToBillsDTO(billRepository.findAll());
	}

	public Bill findById(String billId, String schoolId) throws BillException {
		return billRepository.findByIdAndSchoolId(UUID.fromString(billId),UUID.fromString(schoolId))
				.orElseThrow(() -> new BillException(BillEnum.GET_ERROR));
	}

	public List<BillDTO> findBills(String schoolId, String studentId, String status, Integer month, Integer year) throws StudentException {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Bill> query = criteriaBuilder.createQuery(Bill.class);
		Root<Bill> root = query.from(Bill.class);

		List<Predicate> predicates = new ArrayList<>();
		addField("schoolId", Integer.valueOf(schoolId), predicates, criteriaBuilder, root);
		studentService.findById(studentId).ifPresent(student -> addField("student", student, predicates, criteriaBuilder, root));
		addField("year", year, predicates, criteriaBuilder, root);
		addField("month", month, predicates, criteriaBuilder, root);
		if (status != null) {
			addField("status", Mapper.mapperToEnumPaymentStatus(status), predicates, criteriaBuilder, root);
		}
		query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

		return Mapper.mapperToBillsDTO(entityManager.createQuery(query).getResultList());
	}

	private void addField(String fieldName, Object value, List<Predicate> predicates, CriteriaBuilder criteriaBuilder,
			Root<Bill> root) {

		if (Optional.ofNullable(value).isPresent())
			predicates.add(criteriaBuilder.equal(root.get(fieldName), value));
	}

	public void updatePayment(String schoolId, String billId, BillStatusApi billStatusApi) throws TransactionException {
		Bill bill = findById(billId, schoolId);
		bill.setSchoolId(UUID.fromString(schoolId));
		bill.setStatus(Mapper.mapperToEnumPaymentStatus(billStatusApi.getStatus()));
		bill.setYear(LocalDate.now().getYear());
		bill.setMonth(LocalDate.now().getMonthValue());
		billRepository.save(bill);
	}

}
