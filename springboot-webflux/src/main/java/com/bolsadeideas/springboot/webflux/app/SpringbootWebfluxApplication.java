package com.bolsadeideas.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringbootWebfluxApplication implements CommandLineRunner{

	private final ProductoDao dao;
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootWebfluxApplication.class);
	
	private final ReactiveMongoTemplate mongoTemplate;
	
	public SpringbootWebfluxApplication(ProductoDao dao, ReactiveMongoTemplate mongoTemplate) {
		this.dao = dao;
		this.mongoTemplate = mongoTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();
		
		Flux.just(new Producto("TV Panasonic Pantalla LCD", 456.89),
				new Producto("Sony Camara HD Digital", 177.89),
				new Producto("Apple iPod", 46.59),
				new Producto("Hewlett Packard Multifuncional", 200.89)
				)
		.flatMap(producto -> {
			producto.setCreateAt(new Date());
			return dao.save(producto);
		})
		.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
	}
	
	

}
