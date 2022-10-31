package org.organization.repository;

import java.util.List;

import org.organization.controller.Library;

public interface LibraryRepositoryCustom {
	
	List<Library> findAllByAuthor(String authorName);

	Library findByName(String bookName);

}
