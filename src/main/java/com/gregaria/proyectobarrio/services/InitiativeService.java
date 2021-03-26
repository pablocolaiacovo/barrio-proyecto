/**
 * 
 */
package com.gregaria.proyectobarrio.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregaria.proyectobarrio.Enums.State;
import com.gregaria.proyectobarrio.entities.Initiative;
import com.gregaria.proyectobarrio.entities.Location;
import com.gregaria.proyectobarrio.entities.Tag;
import com.gregaria.proyectobarrio.entities.User;
import com.gregaria.proyectobarrio.errors.WebException;
import com.gregaria.proyectobarrio.repositories.InitiativeRepository;
import com.gregaria.proyectobarrio.repositories.UserRepository;


@Service
public class InitiativeService {
	
	@Autowired
	InitiativeRepository initiativeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TagService tagService;
	
	@Transactional
	public Initiative save(String title,
			 String  creatorId,
			 Integer budget,
			 String description,
			 String idTags, 
			 Location location,
			 State state) {
		
		Initiative initiative = new Initiative();
		
		User creator = userRepository.getOne(creatorId);
		initiative.setCreator(creator);
		
		initiative.setTitle(title);
		initiative.setBudget(budget);
		initiative.setDescription(description);
		
		List<Tag> tags = tagService.idsToList(idTags);
				
		initiative.setTags(tags);
		initiative.setLocation(location);
		initiative.setState(state);
		initiative.setCreatedAt(new Date());
		initiative.setActive(true);
		
		return initiativeRepository.save(initiative);
	}
	
	@Transactional
	public Initiative update(String id, String title,
			 Integer budget,
			 String description,
			 String idTags, 
			 Location location,
			 State state) throws Exception {
		
		Initiative initiative = findById(id);
			
		initiative.setTitle(title);
		initiative.setBudget(budget);
		initiative.setDescription(description);
		
		List<Tag> tags = tagService.idsToList(idTags);
				
		initiative.setTags(tags);
		initiative.setLocation(location);
		initiative.setState(state);
		initiative.setUpdatedAt(new Date());
		
		
		return initiativeRepository.save(initiative);
	}
	
	@Transactional
	public List<Initiative> listActives() {
		return initiativeRepository.findByActiveTrue();
	}
	
	public List<Initiative> listByCreator(String creatorId) {
		return initiativeRepository.findByCreatorId(creatorId);
	}
 	
	public Initiative findById(String id) throws WebException {
		Optional<Initiative> response = initiativeRepository.findById(id);
		
		Initiative initiative = null;
		if (response.isPresent()) {
			 initiative = response.get();
			 
			 return initiative;
		} else {
			throw new WebException("No se encontró la iniciativa");
		}
	}

}
