package de.javatar81.examples.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import de.javatar81.examples.domain.User;
import de.javatar81.examples.service.UserService;

public class UserLazyDataModel extends LazyDataModel<User> implements SelectableDataModel<User> {

	private static final long serialVersionUID = -6123945723069023025L;
	private final transient UserService userService;
	private static final SortOrder DEFAULT_SORT_ORDER = SortOrder.ASCENDING;
	private static final String DEFAULT_SORT_FIELD = "login";

	public UserLazyDataModel(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public Object getRowKey(User user) {
		return user.getId();
	}
	
	@Override
	public User getRowData(String rowKey) {
		Long rowId = Long.valueOf(rowKey);
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) super.getWrappedData();
		return users.stream().filter(user -> user.getId().equals(rowId)).findAny().orElse(null);
	}

	@Override
	public List<User> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		Sort sort = new Sort(getDirection(DEFAULT_SORT_ORDER), DEFAULT_SORT_FIELD);
		if (multiSortMeta != null) {
			List<Order> orders = multiSortMeta.stream()
					.map(m -> new Order(getDirection(m.getSortOrder() != null ? m.getSortOrder() : DEFAULT_SORT_ORDER),
							m.getSortField()))
					.collect(Collectors.toList());
			sort = new Sort(orders);
		}
		return filterAndSort(first, pageSize, filters, sort);
	}

	@Override
	public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		Sort sort = null;
		if (sortField != null) {
			sort = new Sort(getDirection(sortOrder != null ? sortOrder : DEFAULT_SORT_ORDER), sortField);
		} else if (DEFAULT_SORT_FIELD != null) {
			sort = new Sort(getDirection(sortOrder != null ? sortOrder : DEFAULT_SORT_ORDER), DEFAULT_SORT_FIELD);
		}
		return filterAndSort(first, pageSize, filters, sort);
	}
	
	private List<User> filterAndSort(int first, int pageSize, Map<String, Object> filters, Sort sort) {
		Map<String, String> filtersMap = filters.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
		Page<User> page = userService.findByFilter(filtersMap, new PageRequest(first / pageSize, pageSize, sort));
		this.setRowCount(((Number) page.getTotalElements()).intValue());
		this.setWrappedData(page.getContent());
		return page.getContent();
	}

	private static Direction getDirection(SortOrder order) {
		switch (order) {
		case ASCENDING:
			return Direction.ASC;
		case DESCENDING:
			return Direction.DESC;
		case UNSORTED:
		default:
			return null;
		}
	}
}
