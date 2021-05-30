package click.escuela.school.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.school.admin.api.BillApi;
import click.escuela.school.admin.api.BillSearchApi;
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.enumerator.BillEnum;
import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.exception.TransactionException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
import click.escuela.school.admin.repository.BillRepository;
import click.escuela.school.admin.service.BillServiceGeneric;

@Service
public class BillServiceImpl implements BillServiceGeneric<BillApi, BillDTO> {

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private StudentServiceImpl studentService;

	@Override
	public void create(String id, BillApi billApi) throws TransactionException {
		try {
			UUID studentId = UUID.fromString(id);
			Bill bill = Mapper.mapperToBill(billApi);
			bill.setStatus(PaymentStatus.PENDING);
			bill.setStudentId(studentId);
			billRepository.save(bill);
			studentService.addBill(bill, studentId);
		} catch (Exception e) {
			throw new TransactionException(BillEnum.CREATE_ERROR.getCode(), BillEnum.CREATE_ERROR.getDescription());
		}
	}

	@Override
	public BillDTO getById(String billId) throws TransactionException {
		return Mapper.mapperToBillDTO(findById(billId));
	}

	@Override
	public List<BillDTO> findAll() {
		return Mapper.mapperToBillsDTO(billRepository.findAll());
	}

	public Bill findById(String billId) throws TransactionException {
		return billRepository.findById(UUID.fromString(billId)).orElseThrow(
				() -> new TransactionException(BillEnum.GET_ERROR.getCode(), BillEnum.GET_ERROR.getDescription()));
	}

	public List<BillDTO> findBills(BillSearchApi bill, String studentId, Boolean fullDetail) throws TransactionException {
		List<BillDTO> bills = new ArrayList<>();
		try {
			if(Boolean.TRUE.equals(fullDetail)) {
				bills = getBills(bill,studentId);
			}
		}
		catch (Exception e) {
			throw new TransactionException(BillEnum.BAD_BOOLEAN.getCode(), BillEnum.BAD_BOOLEAN.getDescription());
		}
		return bills;
	}
	
	private List<BillDTO> getBills(BillSearchApi bill, String studentId) {
		UUID id = UUID.fromString(studentId);
		if (bill.getYear() != null && bill.getMonth() != null && bill.getStatus() != null) {
			return Mapper.mapperToBillsDTO(billRepository.findByStudentIdAndMonthAndYearAndStatus(id, bill.getMonth(),
					bill.getYear(), Mapper.mapperToEnumPaymentStatus(bill.getStatus())));
		} else if (bill.getStatus() == null) {
			return findBillsByMonthOrByYear(id, bill.getMonth(), bill.getYear());
		} else if (bill.getYear() == null) {
			return findBillsByMonthOrByStatus(id, bill.getMonth(), bill.getStatus());
		} else {
			return findBillsByYearOrByStatus(id, bill.getYear(), bill.getStatus());
		}
	}

	private List<BillDTO> findBillsByYearOrByStatus(UUID id, Integer year, String status) {
		if (year != null && status != null) {
			return Mapper.mapperToBillsDTO(
					billRepository.findByStudentIdAndYearAndStatus(id, year, Mapper.mapperToEnumPaymentStatus(status)));
		} else {
			return (status == null) ? Mapper.mapperToBillsDTO(billRepository.findByStudentIdAndYear(id, year))
					: Mapper.mapperToBillsDTO(
							billRepository.findByStudentIdAndStatus(id, Mapper.mapperToEnumPaymentStatus(status)));
		}
	}

	private List<BillDTO> findBillsByMonthOrByStatus(UUID id, Integer month, String status) {
		if (month != null && status != null) {
			return Mapper.mapperToBillsDTO(billRepository.findByStudentIdAndMonthAndStatus(id, month,
					Mapper.mapperToEnumPaymentStatus(status)));
		} else {
			return (status == null) ? Mapper.mapperToBillsDTO(billRepository.findByStudentIdAndMonth(id, month))
					: Mapper.mapperToBillsDTO(
							billRepository.findByStudentIdAndStatus(id, Mapper.mapperToEnumPaymentStatus(status)));
		}
	}

	private List<BillDTO> findBillsByMonthOrByYear(UUID id, Integer month, Integer year) {
		if (year != null && month != null) {
			return Mapper.mapperToBillsDTO(billRepository.findByStudentIdAndMonthAndYear(id, month, year));
		} else {
			return (year == null) ? Mapper.mapperToBillsDTO(billRepository.findByStudentIdAndMonth(id, month))
					: Mapper.mapperToBillsDTO(billRepository.findByStudentIdAndYear(id, year));
		}
	}

}
