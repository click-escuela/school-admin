package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.exception.BillException;
import click.escuela.school.admin.exception.StudentException;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.repository.BillRepository;
import click.escuela.school.admin.service.impl.BillServiceImpl;
import click.escuela.school.admin.service.impl.StudentServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class BillServiceTest {

	@Mock
	private BillRepository billRepository;

	@Mock
	private StudentServiceImpl studentService;

	@Mock
	private EntityManager entityManager;

	@Mock
	private JpaEntityInformation<Bill, UUID> information;

	@Mock
	private CriteriaBuilder criteriaBuilder;

	@Mock
	private CriteriaQuery<Bill> query;

	@Mock
	private Root<Bill> root;

	@Mock
	private TypedQuery<Bill> typedQuery;
	private BillServiceImpl billServiceImpl = new BillServiceImpl();
	private BillApi billApi;
	private Bill bill;
	private UUID id;
	private UUID studentId;
	private List<Bill> bills;

	@Before
	public void setUp() throws TransactionException, StudentException {
		PowerMockito.mockStatic(Mapper.class);
		studentId = UUID.randomUUID();
		id = UUID.randomUUID();

		bill = Bill.builder().id(id).year(2021).month(6).status(PaymentStatus.PENDING).studentId(studentId).file("Mayo")
				.amount((double) 12000).build();

		billApi = BillApi.builder().year(2021).month(6).file("Mayo").amount((double) 12000).build();

		Optional<Bill> optional = Optional.of(bill);
		bills = new ArrayList<>();
		bills.add(bill);

		BillDTO billDTO = BillDTO.builder().id(id.toString()).year(2021).month(6).status(PaymentStatus.PENDING)
				.file("Mayo").amount((double) 12000).build();
		List<BillDTO> billsDTO = new ArrayList<>();
		billsDTO.add(billDTO);

		Mockito.when(Mapper.mapperToBill(billApi)).thenReturn(bill);
		Mockito.when(Mapper.mapperToBillsDTO(bills)).thenReturn(billsDTO);
		Mockito.when(billRepository.save(bill)).thenReturn(bill);
		Mockito.when(billRepository.findById(id)).thenReturn(optional);
		doNothing().when(studentService).addBill(bill, studentId);

		// inyecta en el servicio el objeto repository
		ReflectionTestUtils.setField(billServiceImpl, "billRepository", billRepository);

		// inyecta en el servicio Bill el servicio Student
		ReflectionTestUtils.setField(billServiceImpl, "studentService", studentService);
		ReflectionTestUtils.setField(billServiceImpl, "entityManager", entityManager);

	}

	@Test
	public void whenCreateIsOk() throws BillException, StudentException {
		billServiceImpl.create("1234", studentId.toString(), billApi);

		verify(billRepository).save(bill);
		verify(studentService).addBill(bill, studentId);

	}

	@Test
	public void whenCreateIsError() {
		UUID idStudentOther = UUID.randomUUID();
		Mockito.when(billRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(BillException.class).isThrownBy(() -> {
			billServiceImpl.create("1234", idStudentOther.toString(), Mockito.any());
		}).withMessage("No se pudo crear la factura correctamente");
	}

	@Test
	public void whenFindBillsIsOk() {
		Mockito.when(information.getJavaType()).thenReturn(Bill.class);
		Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
		Mockito.when(criteriaBuilder.createQuery(Bill.class)).thenReturn(query);
		Mockito.when(entityManager.createQuery(query)).thenReturn(typedQuery);
		Mockito.when(query.from(Bill.class)).thenReturn(root);
		Mockito.when(query.select(root)).thenReturn(query);

		boolean hasError = false;
		try {
			billServiceImpl.findBills("1234", studentId.toString(), PaymentStatus.PENDING.toString(), 2, 2021);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenFindBillsIsError() {
		boolean hasError = false;

		try {
			billServiceImpl.findBills(null, null, null, null, null);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}
}