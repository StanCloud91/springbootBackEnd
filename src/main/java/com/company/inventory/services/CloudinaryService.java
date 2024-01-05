package com.company.inventory.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
	
	Cloudinary cloudinary;
	
	private Map<String,String> valuesMap = new HashMap<>();
	
	/*
	 * import com.cloudinary.*;
...
Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
  "cloud_name", "dopqgqkci",
  "api_key", "971554421466478",
  "api_secret", "P7dLOqLGGhzeRjOuV3yIS5yr_s0"));
	 */
	public CloudinaryService() {
		valuesMap.put("cloud_name", "dopqgqkci");
		valuesMap.put("api_key", "971554421466478");
		valuesMap.put("api_secret", "P7dLOqLGGhzeRjOuV3yIS5yr_s0");
		cloudinary = new Cloudinary(valuesMap);
	}
	
	public Map upload(MultipartFile multipartFile) {
		
		try {
			File file = convert(multipartFile);
			Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
			file.delete();
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String search(String id) {
		String url = cloudinary.url()
                .transformation(new Transformation()) // Puedes aplicar transformaciones si es necesario
                .imageTag(id);
		return url;
	}
	
	public Map delete(String id) {
		try {
			Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public File convert(MultipartFile multipartFile) {
		File file = new File(multipartFile.getOriginalFilename());
		FileOutputStream fo;
		try {
			fo = new FileOutputStream(file);
			fo.write(multipartFile.getBytes());
			fo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}
	
	
	
	
}
