package com.company.inventory.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.response.ProductResponseRest;

@Service
public class ProductServiceImpl implements IProductService {
	
	@Autowired
    private IProductDao ProductDao;
	
	@Autowired
    private ICategoryDao CategoryDao;
	
	@Autowired	
	private CloudinaryService cloudinaryService;
	
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		// TODO Auto-generated method stub
		ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        
        try {
        	Optional<Category> category = CategoryDao.findById(categoryId);
        	if(category.isPresent()) {
        		product.setCategory(category.get());
	        	Product ProductSaved = ProductDao.save(product);        	        		
	        	if(ProductSaved != null)
	        	{
	        		list.add(ProductSaved);
	        		response.getProductResponse().setProduct(list);
	        		response.setMetadata("Respuesta ok","00","Respuesta Exitosa");        	
	        	}else {
	        		response.setMetadata("Respuesta mala","-1","Registro no guardado");                
	                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
        	}else {
        		response.setMetadata("Respuesta mala","-1","No existe la cateogira asignada");                
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Error al guardar");
            e.getStackTrace();
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		// TODO Auto-generated method stub
		ProductResponseRest response = new ProductResponseRest();

        try {
        	Optional<Product> p= ProductDao.findById(id);
        	if(p.isPresent()) {        		
        		ProductDao.deleteById(id);
        		cloudinaryService.delete(p.get().getPhoto());
                response.setMetadata("Respuesta ok","00","Respuesta Exitosa");
        	}
        	
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Respuesta Sin Exito");
            e.getStackTrace();
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);

	}

	@Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> search() {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
        	List<Product> productos = (List<Product>) ProductDao.findAll();
        	if(productos.size()>0) {
        		
        		for (Iterator iterator = productos.iterator(); iterator.hasNext();) {
					Product product = (Product) iterator.next();
					//byte[] imageDescompressed = util.decompressZLib(product.getPicture());
					if(product.getPhoto().equals("NA"))
					{
						//System.out.println("Entro aca");
						product.setPhoto(cloudinaryService.search("gf0y51lvnw2yf6ehqyhd"));
					}else {
						//System.out.println("Entro aca 2");
						product.setPhoto(cloudinaryService.search(product.getPhoto()));
					}
	        		
	        		list.add(product);					
				}        		
        		response.getProductResponse().setProduct(list);
        		response.setMetadata("Respuesta ok","00","Respuesta Exitosa");
        	}else {
        		response.setMetadata("Respuesta mala","-1","Respuesta Sin Informacion");                
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
        	
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Respuesta Sin Exito");
            e.getStackTrace();
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> update(Product product,Long categoryId, Long id) {
		ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        
        try {
        	//Product ProductSaved = ProductDao.save(Product);
        	Optional<Product> ProductSearch = ProductDao.findById(id);
        	
        	if(ProductSearch.isPresent()) {
        		        		
        		Optional<Category> category = CategoryDao.findById(categoryId);
        		if(category.isPresent())
            	{
            		ProductSearch.get().setName(product.getName());
            		ProductSearch.get().setAccount(product.getAccount());
            		ProductSearch.get().setPrice(product.getPrice());
            		ProductSearch.get().setCategory(category.get());
            		//byte[] imageDescompressed = util.decompressZLib(product.getPicture());
            		if(product.getPhoto()!="NA") {
            			ProductSearch.get().setPhoto(product.getPhoto());
            		}
            		
            		
            		Product ProducttoUpdate = ProductDao.save(ProductSearch.get());
            		if(ProducttoUpdate != null)
                	{
    	        		list.add(ProducttoUpdate);
    	        		response.getProductResponse().setProduct(list);
    	        		response.setMetadata("Respuesta ok","00","Respuesta Exitosa");
                	}else {
                		response.setMetadata("Respuesta mala","-1","Registro no actualizado");                
                        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
    				}	
            	}else {
            		response.setMetadata("Respuesta mala","-1","Registro no actualizado");                
                    return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
    			}
        	}else {
        		response.setMetadata("Respuesta mala","-1","Registro no actualizado");                
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
        	
        }catch (Exception e){
            response.setMetadata("Respuesta mala","-1","Error al guardar");
            e.getStackTrace();
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}

}
