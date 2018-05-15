package drink.machine.drinks;

import drink.machine.graphql.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractService<T> {

    @PersistenceContext(unitName = "drink-machine-pu")
    private EntityManager em;

    protected final <E extends PaginateResult<T>> E list(Class<T> type, Class<E> result, Pagination pagination, List<Sort> sort, List<Criteria> criterias) {
        Pagination safePagination = pagination == null? new Pagination() : pagination;
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> root = criteriaQuery.from(type);

        return result(
                paginate(
                        orderBy(
                                filter(criterias, criteriaQuery, criteriaBuilder, root),
                                sort, criteriaBuilder, root),
                        pagination),
                safePagination, type, result, criteriaBuilder);
    }

    @NotNull
    private <E extends PaginateResult<T>> E result(TypedQuery<T> typedQuery, Pagination safePagination, Class<T> type, Class<E> result, CriteriaBuilder criteriaBuilder) {
        E paginateResult = null;
        try {
            paginateResult = result.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        paginateResult.values = typedQuery.getResultList();
        paginateResult.result = new Result();
        paginateResult.result.count = paginateResult.values.size();
        paginateResult.result.offset = safePagination.offset;

        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        count.select(criteriaBuilder.count(count.from(type)));

        paginateResult.result.totalCount = em.createQuery(count).getSingleResult().intValue();
        return paginateResult;
    }

    private TypedQuery<T> orderBy(CriteriaQuery<T> query, List<Sort> sort, CriteriaBuilder criteriaBuilder, Root<T> root) {
        query.select(root);
        if (sort != null && !sort.isEmpty()) {
            query.orderBy(sort.stream().map(s -> {
                        if (s.direction == Sort.SortDirection.ASC) {
                            return criteriaBuilder.asc(root.get(s.property));
                        } else {
                            return criteriaBuilder.desc(root.get(s.property));
                        }
                    }).collect(Collectors.toList())
            );
        }
        return em.createQuery(query);
    }

    private TypedQuery<T> paginate(TypedQuery<T> typedQuery, Pagination pagination) {
        return typedQuery.setFirstResult(pagination.offset)
                .setMaxResults(pagination.limit);
    }

    private CriteriaQuery<T> filter(List<Criteria> criterias, CriteriaQuery<T> criteriaQuery, CriteriaBuilder criteriaBuilder, Root<T> root) {
        if (criterias != null && !criterias.isEmpty()) {
            Predicate predicate = criteriaBuilder.conjunction();
            for (Criteria c : criterias) {
                if (c.operator.equals(Criteria.Operator.EQ)) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.equal(root.get(c.property),
                                    c.argument));
                }
            }
            criteriaQuery.where(predicate);
        }
        return criteriaQuery;
    }
}
