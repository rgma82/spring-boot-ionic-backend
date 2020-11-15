package com.rafael.medeiros.cursomsc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.rafael.medeiros.cursomsc.domain.Cliente;
import com.rafael.medeiros.cursomsc.domain.enums.TipoCliente;
import com.rafael.medeiros.cursomsc.dto.ClienteNewDTO;
import com.rafael.medeiros.cursomsc.repositories.ClienteRepository;
import com.rafael.medeiros.cursomsc.resources.exception.FieldMessage;
import com.rafael.medeiros.cursomsc.services.validation.utils.BR;



public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		if(objDto.getTipo() == null) {
			list.add(new FieldMessage("tipo","O Tipo nao pode ser nulo"));
		}
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod())&& !BR.isValidCPF(objDto.getCpfOCnpj()) ) {
			
			list.add(new FieldMessage("tipo","Numero de CPF invalido!"));
		}
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod())&& !BR.isValidCNPJ(objDto.getCpfOCnpj()) ) {
			
			list.add(new FieldMessage("tipo","Numero de CNPJ invalido!"));
		}

		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null ) {
			list.add(new FieldMessage("email","Este email ja esta sendo usado no sistema!"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}
