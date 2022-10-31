package org.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.organization.controller.Library;
@Repository
public interface LibraryRepository extends JpaRepository<Library, String>,LibraryRepositoryCustom{

}
