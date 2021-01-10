package com.seth.waxandwanerecords.entities;


	import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.dom4j.tree.AbstractEntity;
import org.springframework.security.core.GrantedAuthority;

	@Entity
	public class Role extends AbstractEntity implements GrantedAuthority{
		
		@Id
		private int id;
		private String name;
		@ManyToMany(mappedBy="role")
		private Set<User> user;
		
		public Set<User> getUsers() {
			return user;
		}

		public void setUsers(Set<User> users) {
			this.user = users;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String getAuthority() {
			// TODO Auto-generated method stub
			return null;
		}
		
}
