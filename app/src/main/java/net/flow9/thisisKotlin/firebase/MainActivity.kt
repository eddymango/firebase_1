package net.flow9.thisisKotlin.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import net.flow9.thisisKotlin.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val database = Firebase.database("https://eddymangotest1-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

        setContentView(binding.root)

        myRef.addValueEventListener( object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //먼저 textList 지우기
                binding.textList.setText("")

                for ( item in snapshot.children){
                    //item의 value를 꺼내서 User 클래스로 캐스팅
                    // value가 없을 수도 있기 때문에 let 스코프 함수로 처리

                    item.getValue(User::class.java)?.let { user ->
                        binding.textList.append("${user.name} : ${user.password} \n")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }

        }
        )

        with(binding){
            btnPost.setOnClickListener{
                val name = editName.text.toString()
                val password = editPassword.text.toString()
                val user = User(name, password)
                addItem(user)
            }
        }



    }

    fun addItem(user:User){
        val id = myRef.push().key!!
        user.id = id
        myRef.child(id).setValue(user)

    }
}

class User{
    var id:String = ""
    var name:String = ""
    var password:String = ""
    constructor()

    constructor(name:String,password:String){
        this.name = name
        this.password = password
    }


}
