package info.fortheease.seendial;

import android.app.Application;

import java.util.List;

public class UserRepository {
    private final UserDAO userDAO;
    private final List<User> allUsers;

    public UserRepository(Application application){
        UserDatabase userDatabase = UserDatabase.getInstance(application);
        userDAO = userDatabase.userDAO();
        allUsers = userDAO.getAllUsers();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDAO, user).runnable.run();
    }

    public void update(User user) {
        new UpdateUserAsyncTask(userDAO, user).runnable.run();
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(userDAO, user).runnable.run();
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    private static class InsertUserAsyncTask{
        private final UserDAO userDAO;
        private final User[] users;

        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                insert();
            }

            private void insert(){
                userDAO.insert(users[0]);
            }
        };
        private InsertUserAsyncTask(UserDAO userDAO, User... users){
            this.userDAO = userDAO;
            this.users = users;
        }
    }

    private static class UpdateUserAsyncTask{
        private final UserDAO userDAO;
        private final User[] users;

        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                update();
            }

            private void update(){
                userDAO.update(users[0]);
            }
        };
        private UpdateUserAsyncTask(UserDAO userDAO, User... users){
            this.userDAO = userDAO;
            this.users = users;
        }
    }

    private static class DeleteUserAsyncTask{
        private final UserDAO userDAO;

        private final User[] users;

        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                delete();
            }

            private void delete(){
                userDAO.delete(users[0]);
            }
        };
        private DeleteUserAsyncTask(UserDAO userDAO, User... users){
            this.userDAO = userDAO;
            this.users = users;
        }
    }

}