package click.escuela.school.admin.service.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import click.escuela.school.admin.enumerator.GenderType;
import click.escuela.school.admin.enumerator.ParentMessage;
import click.escuela.school.admin.exception.ParentException;
import click.escuela.school.admin.model.Parent;
import click.escuela.school.admin.repository.ParentRepository;

@Service
public class ParentServiceImpl {

	@Autowired
	private ParentRepository parentRepository;

	public Optional<Parent> findById(String parentId) throws ParentException {
		return Optional.of(parentRepository.findById(UUID.fromString(parentId)))
				.orElseThrow(() -> new ParentException(ParentMessage.GET_ERROR));
	}	
	public Optional<Parent> findByOptions(String name, String surname, String document, GenderType gender){
		return parentRepository.findByNameAndSurnameAndDocumentAndGender(name, surname, document, gender);
	}


}
