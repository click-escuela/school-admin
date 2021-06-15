package click.escuela.school.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
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
import click.escuela.school.admin.dto.BillDTO;
import click.escuela.school.admin.enumerator.BillEnum;
import click.escuela.school.admin.enumerator.PaymentStatus;
import click.escuela.school.admin.exception.BillException;
import click.escuela.school.admin.mapper.Mapper;
import click.escuela.school.admin.model.Bill;
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
			UUID studentId = UUID.fromString(id);
			Bill bill = Mapper.mapperToBill(billApi);
			bill.setSchoolId(Integer.valueOf(schoolId));
			bill.setStatus(PaymentStatus.PENDING);
			bill.setStudentId(studentId);
			billRepository.save(bill);
			studentService.addBill(bill, studentId);
		} catch (Exception e) {
			throw new BillException(BillEnum.CREATE_ERROR);
		}
	}

	@Override
	public BillDTO getById(String billId) throws BillException {
		return Mapper.mapperToBillDTO(findById(billId));
	}

	@Override
	public List<BillDTO> findAll() {
		return Mapper.mapperToBillsDTO(billRepository.findAll());
	}

	public Bill findById(String billId) throws BillException {
		return billRepository.findById(UUID.fromString(billId)).orElseThrow(
				() -> new BillException(BillEnum.GET_ERROR));
	}


	public List<BillDTO> findBills( String schoolId, String studentId, String status, Integer month, Integer year){
				
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Bill> query= criteriaBuilder.createQuery(Bill.class);
		Root<Bill> root = query.from(Bill.class);
		
		List<Predicate> predicates = new ArrayList<>();
		addField("schoolId", Integer.valueOf(schoolId),predicates, criteriaBuilder, root);
		addField("studentId", UUID.fromString(studentId) , predicates, criteriaBuilder, root);
		addField("year", year, predicates, criteriaBuilder, root);
		addField("month", month, predicates, criteriaBuilder, root);
		if(status!=null) {
			addField("status", Mapper.mapperToEnumPaymentStatus(status), predicates, criteriaBuilder, root);
		}
		query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
		
		return Mapper.mapperToBillsDTO(entityManager.createQuery(query).getResultList()); 		
	}
	
	private void addField(String fieldName, Object value, List<Predicate> predicates,
			CriteriaBuilder criteriaBuilder,Root<Bill> root ) {
		
		if(Optional.ofNullable(value).isPresent())
			predicates.add(criteriaBuilder.equal (root.get(fieldName), value));
	}

}
