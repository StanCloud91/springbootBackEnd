package com.company.inventory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements ICategoryService{

	@Autowired
    private ICategoryDao categoryDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponseRest> search() {

        CategoryResponseRest response = new CategoryResponseRest();

        try {
            List<Category> category = (List<Category>) categoryDao.findAll();
            response.getCategoryResponse().setCategory(category);
            response.setMetadata("Respuesta ok","00","Respuesta Exitosa");
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Respuesta Sin Exito");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

    @Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		// TODO Auto-generated method stub
        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();
        
        try {
        	Optional<Category> category = categoryDao.findById(id);
        	if(category.isPresent()) {
        		list.add(category.get());
        		response.getCategoryResponse().setCategory(list);
        		response.setMetadata("Respuesta ok","00","Respuesta Exitosa");
        	}else {
        		response.setMetadata("Respuesta mala","-1","Respuesta Sin Informacion");                
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
        	
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Respuesta Sin Exito");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

    @Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		// TODO Auto-generated method stub
		CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();
        
        try {
        	Category categorySaved = categoryDao.save(category);        	        		
        	if(categorySaved != null)
        	{
        		list.add(categorySaved);
        		response.getCategoryResponse().setCategory(list);
        		response.setMetadata("Respuesta ok","00","Respuesta Exitosa");        	
        	}else {
        		response.setMetadata("Respuesta mala","-1","Registro no guardado");                
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Error al guardar");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

    @Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
		// TODO Auto-generated method stub
		CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();
        
        try {
        	//Category categorySaved = categoryDao.save(category);
        	Optional<Category> categorySearch = categoryDao.findById(id);
        	if(categorySearch != null)
        	{
        		categorySearch.get().setName(category.getName());
        		categorySearch.get().setDescription(category.getDescription());
        		
        		Category categorytoUpdate = categoryDao.save(categorySearch.get());
        		if(categorytoUpdate != null)
            	{
	        		list.add(categorytoUpdate);
	        		response.getCategoryResponse().setCategory(list);
	        		response.setMetadata("Respuesta ok","00","Respuesta Exitosa");
            	}else {
            		response.setMetadata("Respuesta mala","-1","Registro no actualizado");                
                    return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
				}	
        	}else {
        		response.setMetadata("Respuesta mala","-1","Registro no actualizado");                
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Error al guardar");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

    @Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> delete(Long id) {
		// TODO Auto-generated method stub
		CategoryResponseRest response = new CategoryResponseRest();

        try {
            categoryDao.deleteById(id);        	
            response.setMetadata("Respuesta ok","00","Respuesta Exitosa");
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Respuesta Sin Exito");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);

	}
	
	

}
