package core.basesyntax.dao.animal;

import core.basesyntax.dao.AbstractDao;
import core.basesyntax.model.zoo.Animal;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class AnimalDaoImpl extends AbstractDao implements AnimalDao {
    public AnimalDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Animal save(Animal animal) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(animal);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new RuntimeException("Failed to persist entity " + animal, e);
        }

        return animal;
    }

    @Override
    public List<Animal> findByNameFirstLetter(Character character) {
        try (Session session = sessionFactory.openSession()) {
            Query<Animal> query = session.createQuery(
                    "from Animal where LOWER(name) LIKE :value", Animal.class);
            query.setParameter("value", Character.toLowerCase(character) + "%");

            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to select animal by first letter in the name: "
                    + character, e);
        }
    }
}
