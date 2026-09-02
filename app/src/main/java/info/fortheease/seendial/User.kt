package info.fortheease.seendial

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class User(
    @ColumnInfo(name = "phoneNumber")
    val phoneNum: String,
    
    @ColumnInfo(name = "profilePhoto")
    val imgAddress: String
) {
    @PrimaryKey
    var uid: String = imgAddress
}
