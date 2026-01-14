package info.fortheease.seendial;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase instance;

    public abstract UserDAO userDAO();

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserDatabase.class, "user_database.db")
                    .fallbackToDestructiveMigration(true)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
