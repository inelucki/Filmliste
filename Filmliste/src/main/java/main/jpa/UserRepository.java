package main.jpa;

import org.springframework.data.repository.CrudRepository;

import main.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	/*
	 * benoetigt @EnableGlobalMethodSecurity(prePostEnabled=true) in WebSecurityConfig
	 * returnObject? -> prueft ob der rueckgabewert der methode != null ist
	 * .id -> liest die id des gefundenen Users
	 * principal? -> @Currentuser, erfragt die id des aktuellen users (falls vorhanden)
	 * wenn die beiden ids gleich sind, wird der wert zurueck gegeben
	 * 
	 * genaue erklaerung : https://www.youtube.com/watch?v=qoR6lY6biO4 ab min 43 ca
	 */
	//@PostAuthorize("returnObject?.id == principal?.id")
	
	/*
	 * statt mit query ueber permissionevaluator. eigene klasse in der man die 
	 * Methode hasPermission proggen kann
	 * genaue erklaerung : https://www.youtube.com/watch?v=qoR6lY6biO4 ab min 49 ca
	 */
	//@PostAuthorize("hasPermission(returnObject, 'read'")
	User findByName(String name);
}
