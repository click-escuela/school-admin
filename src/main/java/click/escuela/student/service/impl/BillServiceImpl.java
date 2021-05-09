package click.escuela.student.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.student.api.BillApi;
import click.escuela.student.dto.BillDTO;
import click.escuela.student.enumerator.BillEnum;
import click.escuela.student.enumerator.PaymentStatus;
import click.escuela.student.exception.TransactionException;
import click.escuela.student.mapper.Mapper;
import click.escuela.student.model.Bill;
import click.escuela.student.repository.BillRepository;
import click.escuela.student.service.BillServiceGeneric;

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
		Optional<Bill> optional = billRepository.findById(UUID.fromString(billId));
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new TransactionException(BillEnum.GET_ERROR.getCode(), BillEnum.GET_ERROR.getDescription());
		}

	}

}
