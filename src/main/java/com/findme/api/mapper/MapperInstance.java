package com.findme.api.mapper;

import java.util.List;

public interface MapperInstance <D, E> {
	
	public D toDTO(E entity);
	
	public List<D> toDTO(List<E> entityList);
	
	public E toEntity(D dto);
	
	public List<E> toEntity(List<D> dtoList);
}
