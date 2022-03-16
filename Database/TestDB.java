package Database;

import org.zoodb.internal.server.DiskAccessOneFile;
import org.zoodb.jdo.ZooJdoHelper;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

public class TestDB {

    private PersistenceManager pm;

    public Extent<TestModel> Users;

    private static final String DB_FILE_path = Paths.get("").toAbsolutePath().toString()+"\\TestDB";


    public void insertUser(TestModel Report) {
        SetUp();

        Report.setId(FindHigestID());




        PersistenceManager pm = ZooJdoHelper.openDB(DB_FILE_path);
        pm.currentTransaction().begin();
        pm.setMultithreaded(true);
        //Storing the User In the Database
        pm.makePersistent(Report);



        pm.currentTransaction().commit();
        closeDB(pm);
        pm = null;



    }


    public ArrayList<TestModel> queryForReports() {

        ArrayList<TestModel> users = new ArrayList<>();

        SetUp();

        pm = ZooJdoHelper.openDB(DB_FILE_path);
        pm.currentTransaction().setNontransactionalRead(true);
        pm.setMultithreaded(true);


        Users = pm.getExtent(TestModel.class);
        System.out.println(">> Query for People instances returned results:");
        for (TestModel user : Users) {
            users.add(user);
        }

        closeDB(pm);
        pm = null;



        return users;

    }



    public  void DeleteUser(int id) {

        SetUp();


        pm = ZooJdoHelper.openDB(DB_FILE_path);
        pm.currentTransaction().begin();
        pm.setMultithreaded(true);
        Query q = pm.newQuery(TestModel.class, "id == " + id);
        Collection<TestModel> params = (Collection<TestModel>)q.execute();

        for(TestModel d : params)
        {
            pm.deletePersistent(d);
        }

        pm.currentTransaction().commit();
        closeDB(pm);
        pm = null;

    }


    public void CreateDb() {
        SetUp();

        if(!ZooJdoHelper.dbExists(DB_FILE_path))

            ZooJdoHelper.openOrCreateDB(DB_FILE_path);

    }



    private static void closeDB(PersistenceManager pm) {
        if (pm.currentTransaction().isActive()) {
            pm.currentTransaction().rollback();
        }
        pm.close();
        pm.getPersistenceManagerFactory().close();
    }


    public int FindLowestID(){

        int min = FindHigestID()-1;

        SetUp();

        pm = ZooJdoHelper.openDB(DB_FILE_path);
        pm.currentTransaction().setNontransactionalRead(true);
        pm.setMultithreaded(true);

        Extent<TestModel> courses = pm.getExtent(TestModel.class);

        for (TestModel c: courses) {
            if (c.getId() < min) {
                min = c.getId();
            }
        }

        closeDB(pm);
        pm = null;

        return min;
    }


    public void SetUp(){

        DiskAccessOneFile.allowReadConcurrency(true);
    }


    public int FindHigestID() {
        SetUp();

        pm = ZooJdoHelper.openDB(DB_FILE_path);
        pm.currentTransaction().setNontransactionalRead(true);
        pm.setMultithreaded(true);

        Extent<TestModel> courses = pm.getExtent(TestModel.class);
        TestModel maxID = null;
        int max = -1;
        for (TestModel c: courses) {
            if (c.getId() > max) {
                max = c.getId();
                maxID = c;
            }
        }

        closeDB(pm);
        pm = null;

        return max+1;
    }
}
