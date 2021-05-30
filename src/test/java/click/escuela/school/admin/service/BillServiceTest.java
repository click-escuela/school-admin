package click.escuela.school.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.enumerator.PaymentStatus;
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

	private BillServiceImpl billServiceImpl = new BillServiceImpl();
	private BillApi billApi;
	private UUID id;
	private UUID studentId;

	@Before
	public void setUp() throws TransactionException {
		PowerMockito.mockStatic(Mapper.class);
		studentId = UUID.randomUUID();
		id = UUID.randomUUID();

		Bill bill = Bill.builder().id(id).year(2021).month(6).status(PaymentStatus.PENDING).studentId(studentId).file("Mayo")
				.amount((double) 12000).build();

		billApi = BillApi.builder().year(2021).month(6).file("Mayo").amount((double) 12000).build();

		Optional<Bill> optional = Optional.of(bill);

		Mockito.when(Mapper.mapperToBill(billApi)).thenReturn(bill);

		Mockito.when(billRepository.save(bill)).thenReturn(bill);
		Mockito.when(billRepository.findById(id)).thenReturn(optional);

		doNothing().when(studentService).addBill(bill, studentId);

		// inyecta en el servicio el objeto repository
		ReflectionTestUtils.setField(billServiceImpl, "billRepository", billRepository);

		// inyecta en el servicio Bill el servicio Student
		ReflectionTestUtils.setField(billServiceImpl, "studentService", studentService);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			billServiceImpl.create("1234",studentId.toString(), billApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() {
		UUID idStudentOther = UUID.randomUUID();
		Mockito.when(billRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			billServiceImpl.create("1234",idStudentOther.toString(), Mockito.any());
		}).withMessage("No se pudo crear la factura correctamente");

	}

}