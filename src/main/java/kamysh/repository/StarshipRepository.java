package kamysh.repository;

import kamysh.entity.LoadStarship;
import kamysh.entity.Starship;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class StarshipRepository{

    private final SessionFactory sessionFactory;

    public StarshipRepository() {
        this.sessionFactory = SessionFactoryBuilder.getSessionFactory();
    }

    public void save(LoadStarship starship) {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        try {
            session.save(starship);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public Starship findById(int id) {
        Session session = sessionFactory.openSession();
        return session.get(Starship.class, id);
    }

    public void delete(int starshipiId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.createQuery("delete from kamysh.entity.LoadStarship where starship=:id").setParameter("id", starshipiId).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public int getCountSpaceMarineInStarship(int starshipiId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            return session.createQuery("select count(id) from kamysh.entity.LoadStarship where starship=:id").setParameter("id", starshipiId).executeUpdate();
        } catch (Exception e) {
            transaction.rollback();
            return 0;
        }
    }
}
