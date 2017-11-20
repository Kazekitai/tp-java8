package java8.ex04;


import java8.data.Account;
import java8.data.Data;
import java8.data.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Exercice 04 - FuncCollection
 * Exercice synthèse des exercices précédents
 */
public class Lambda_04_Test {

    // tag::interfaces[]
    interface GenericPredicate<T> {
        boolean test(T t);
    }

    interface GenericMapper<T, E> {
        E map(T t);
    }

    interface Processor<T> {
        void process(T t);
    }
    // end::interfaces[]

    // tag::FuncCollection[]
    class FuncCollection<T> {

        private Collection<T> list = new ArrayList<>();

        public void add(T a) {
            list.add(a);
        }

        public void addAll(Collection<T> all) {
            for(T el:all) {
                list.add(el);
            }
        }
    // end::FuncCollection[]

        // tag::methods[]
        private FuncCollection<T> filter(GenericPredicate<T> predicate) {
            FuncCollection<T> result = new FuncCollection<>();
            for (T t : list) {
				if (predicate.test(t)) {
					result.add(t);
				}
			}
            return result;
        }

        private <E> FuncCollection<E> map(GenericMapper<T, E> mapper) {
            FuncCollection<E> result = new FuncCollection<>();
            for (T t : list) {
				result.add(mapper.map(t));
			}
            return result;
        }

        private void forEach(Processor<T> processor) {
        	for (T t : list) {
				processor.process(t);
			}
        }
        // end::methods[]

    }



    // tag::test_filter_map_forEach[]
    @Test
    public void test_filter_map_forEach() throws Exception {

        List<Person> personList = Data.buildPersonList(100);
        FuncCollection<Person> personFuncCollection = new FuncCollection<>();
        personFuncCollection.addAll(personList);

        Processor<Account> verify =  (c) -> {
        	assert c.getBalance() == 1000 && c.getOwner().getAge()>50;
        };
        personFuncCollection
                // TODO filtrer, ne garder uniquement que les personnes ayant un age > 50
                .filter(p -> p.getAge()>50)
                // TODO transformer la liste de personnes en liste de comptes. Un compte a par défaut un solde à 1000.
                .map(p -> {
                	Account ac = new Account();
                	ac.setOwner(p);
                	ac.setBalance(1000);
                	return ac;
                })
                // TODO vérifier que chaque compte a un solde à 1000.
                // TODO vérifier que chaque titulaire de compte a un age > 50
//                .forEach(null);
//                .forEach( c -> verify.process(c));
                .forEach( (ac) -> {
                	assert ac.getBalance() == 1000 && ac.getOwner().getAge()>50;
                });


        // TODO à supprimer
//        assert false;
    }
    // end::test_filter_map_forEach[]

    // tag::test_filter_map_forEach_with_vars[]
    @Test
    public void test_filter_map_forEach_with_vars() throws Exception {

        List<Person> personList = Data.buildPersonList(100);
        FuncCollection<Person> personFuncCollection = new FuncCollection<>();
        personFuncCollection.addAll(personList);

        // TODO créer un variable filterByAge de type GenericPredicate
        // TODO filtrer, ne garder uniquement que les personnes ayant un age > 50
        // ??? filterByAge = ???;
        GenericPredicate<Person> filterByAge = p -> p.getAge() > 50;

        // TODO créer un variable mapToAccount de type GenericMapper
        // TODO transformer la liste de personnes en liste de comptes. Un compte a par défaut un solde à 1000.
        // ??? mapToAccount = ???;
        GenericMapper<Person, Account> mapToAccount = p -> {
			Account a = new Account();
			a.setOwner(p);
			a.setBalance(1000);
			return a;
		};

        // TODO créer un variable verifyAccount de type GenericProcessor
        // TODO vérifier que chaque compte a un solde à 1000.
        // TODO vérifier que chaque titulaire de compte a un age > 50
        // ??? verifyAccount = ???;
		Processor<Account> verifyAccount = a -> {
			assert a.getBalance() == 1000 && a.getOwner().getAge() > 50;
		};

        /* TODO Décommenter */
        personFuncCollection
                .filter(filterByAge)
                .map(mapToAccount)
                .forEach(verifyAccount);
        

        // TODO A supprimer
//        assert false;
    }
    // end::test_filter_map_forEach_with_vars[]


}
