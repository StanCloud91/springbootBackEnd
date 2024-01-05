package com.company.inventory.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.CloudinaryService;
import com.company.inventory.services.IProductService;
import com.company.inventory.model.Product;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {
	
	@Autowired	
	private IProductService service;
	
	@Autowired	
	private CloudinaryService cloudinaryService;
	
	
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
			@RequestParam("photo") MultipartFile picture,
			@RequestParam("name") String name,
			@RequestParam("price") int price,
			@RequestParam("account") int account,
			@RequestParam("categoryId") Long categoryID
			) throws IOException
	{
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setAccount(account);
		product.setPhoto("NA");
		
		if(!picture.isEmpty()) {
			Map result = cloudinaryService.upload(picture);
			
			product.setPhoto(result.get("public_id").toString());
		}
		
		//product.setPicture(util.compressZLib(picture.getBytes()));
				
		ResponseEntity<ProductResponseRest> response = service.save(product,categoryID);
		return response;
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> update(
			@PathVariable Long id,
			@RequestParam(value = "photo", required = false) MultipartFile picture,
			@RequestParam("name") String name,
			@RequestParam("price") int price,
			@RequestParam("account") int account,
			@RequestParam("categoryId") Long categoryID
			) throws IOException
	{
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setAccount(account);
		product.setPhoto("NA");
		
		if(picture!= null) {
			Map result = cloudinaryService.upload(picture);
			
			product.setPhoto(result.get("public_id").toString());
		}
		
		//product.setPicture(util.compressZLib(picture.getBytes()));
				
		ResponseEntity<ProductResponseRest> response = service.update(product,categoryID,id);
		return response;
	}
	
	@GetMapping("/products")
	public ResponseEntity<ProductResponseRest> search(){
		ResponseEntity<ProductResponseRest> response = service.search();
		return response;
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> deleteById(@PathVariable Long id){
		ResponseEntity<ProductResponseRest> response = service.deleteById(id);
		return response;
	}
}
