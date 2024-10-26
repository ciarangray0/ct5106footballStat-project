package com.ct5106.swishStats.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ct5106.swishStats.domain.AppUser;

@RepositoryRestResource(exported = false)
public interface AppUserRepository extends CrudRepository<AppUser, Long>
{
	Optional<AppUser> findByUsername(String username);
}
