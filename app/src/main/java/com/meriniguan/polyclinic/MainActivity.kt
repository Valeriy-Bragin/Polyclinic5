package com.meriniguan.polyclinic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.meriniguan.polyclinic.databinding.ActivityMainBinding
import com.meriniguan.polyclinic.model.AppDatabase
import com.meriniguan.polyclinic.model.AppRepository
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.doctor.room.Doctor
import com.meriniguan.polyclinic.model.doctor.room.utils.Niche
import com.meriniguan.polyclinic.model.doctor.room.utils.Qualification
import com.meriniguan.polyclinic.model.patient.room.Patient
import com.meriniguan.polyclinic.model.RoomAppRepository
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.patient.room.utils.Condition
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var repo: AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        //val database = Room.databaseBuilder(this, AppDatabase::class.java, "polyclinic_db").build()
        prepopulateData()

        val navController = this.findNavController(R.id.nav_host_fragment)
        // Find reference to bottom navigation view
        val navView: BottomNavigationView = binding.bottomNavView
        // Hook your navigation controller to bottom navigation view
        navView.setupWithNavController(navController)

    }

    private fun prepopulateData() {
        GlobalScope.launch {
            /*val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "polyclinic_db").build()

            val patientsDao = database.getPatientsDao()
            val socialStatusesDao = database.getSocialStatusesDao()
            val conditionsDao = database.getConditionsDao()
            val doctorsDao = database.getDoctorsDao()
            val nichesDao = database.getNichesDao()
            val qualificationsDao = database.getQualificationsDao()
            val diagnosesDao = database.getDiagnosesDao()
            val deceasesDao = database.getDeceasesDao()

            val ioDispatcher = Dispatchers.IO

            val repo = RoomAppRepository(patientsDao, socialStatusesDao, conditionsDao, doctorsDao, nichesDao, qualificationsDao, diagnosesDao, deceasesDao, ioDispatcher)
            */

            repo.addSocialStatus(SocialStatus("schoolkid"))
            repo.addSocialStatus(SocialStatus("student"))
            repo.addSocialStatus(SocialStatus("worker"))
            repo.addSocialStatus(SocialStatus("pensioner"))

            repo.addCondition(Condition("ill"))
            repo.addCondition(Condition("healed"))
            repo.addCondition(Condition("died"))

            repo.addPatient(Patient("Vasya", pSocialStatusId = 4, pConditionId = 1)) // p1
            repo.addPatient(Patient("Petya", pSocialStatusId = 4, pConditionId = 1)) // p2
            repo.addPatient(Patient("Kolya", pSocialStatusId = 4, pConditionId = 2)) // p3
            repo.addPatient(Patient("Vladimir", pSocialStatusId = 3, pConditionId = 3)) // p4
            repo.addPatient(Patient("Sasha", pSocialStatusId = 3, pConditionId = 2)) // p5
            repo.addPatient(Patient("Farid", pSocialStatusId = 2, pConditionId = 2)) // p6
            repo.addPatient(Patient("Kostya", pSocialStatusId = 1, pConditionId = 2)) // p7
            repo.addPatient(Patient("Nadya", pSocialStatusId = 1, pConditionId = 2)) // p8
            repo.addPatient(Patient("Ron", pSocialStatusId = 1, pConditionId = 1)) // p9
            repo.addPatient(Patient("Egor", pSocialStatusId = 2, pConditionId = 1)) // p10
            repo.addPatient(Patient("Yefim", pSocialStatusId = 4, pConditionId = 3)) // p11


            repo.addNiche(Niche("neurolog")) // niche 1
            repo.addNiche(Niche("oftalmolog")) // niche 2
            repo.addNiche(Niche("lor")) // niche 3
            repo.addNiche(Niche("surgeon")) // niche 4
            repo.addNiche(Niche("feldsher")) // niche 5
            repo.addNiche(Niche("medsister")) // niche 6

            repo.addQualification(Qualification("first"))
            repo.addQualification(Qualification("second"))
            repo.addQualification(Qualification("third"))

            repo.addDoctor(Doctor("Inna", dNicheId = 6, dQualificationId = 1)) // doc1
            repo.addDoctor(Doctor("Yulya", dNicheId = 3, dQualificationId = 2)) // doc2
            repo.addDoctor(Doctor("Masha", dNicheId = 5, dQualificationId = 1)) // doc3
            repo.addDoctor(Doctor("Katya", dNicheId = 5, dQualificationId = 3)) // doc4
            repo.addDoctor(Doctor("Gleb", dNicheId = 1, dQualificationId = 2)) // doc5
            repo.addDoctor(Doctor("Milya", dNicheId = 3, dQualificationId = 1)) // doc6
            repo.addDoctor(Doctor("Eugen", dNicheId = 5, dQualificationId = 1)) // doc7


            repo.addDecease(Decease("gripp")) // dec1
            repo.addDecease(Decease("vetryanka")) // dec2
            repo.addDecease(Decease("cold")) // dec3
            repo.addDecease(Decease("shiza")) // dec4
            repo.addDecease(Decease("neuroz")) // dec5
            repo.addDecease(Decease("pervert")) // dec6

            repo.addDiagnosis(Diagnosis(6, 3, 2))
            repo.addDiagnosis(Diagnosis(1, 2, 1))
            repo.addDiagnosis(Diagnosis(6, 4, 6))
            repo.addDiagnosis(Diagnosis(1, 7, 3))
            repo.addDiagnosis(Diagnosis(2, 1, 9))
            repo.addDiagnosis(Diagnosis(5, 7, 2))
            repo.addDiagnosis(Diagnosis(1, 1, 9))
            repo.addDiagnosis(Diagnosis(6, 2, 7))
            repo.addDiagnosis(Diagnosis(1, 7, 2))
            repo.addDiagnosis(Diagnosis(5, 3, 11))
            repo.addDiagnosis(Diagnosis(4, 6, 6))
            repo.addDiagnosis(Diagnosis(2, 3, 2))
            repo.addDiagnosis(Diagnosis(1, 6, 1))
            repo.addDiagnosis(Diagnosis(1, 7, 11))
            repo.addDiagnosis(Diagnosis(5, 4, 11))
            repo.addDiagnosis(Diagnosis(5, 4, 8))

            repo.getDiagnoses(1, 3).collectLatest {
                Log.i("!@#doct", "here")
                for (i in it) {
                    Log.i("!@#doctors_yeeey", i.toString())
                }
            }

            /*repo.getDiagnosesWithDoctors(7).collectLatest {
                for (i in it) {
                    Log.i("!@#kostyaaa", "${i.decease} ${i.docName} ${i.docNiche} ${i.docQualification} ${i.docId}")
                }
            }*/

            /*val ids = mutableListOf<Long>()
            repo.getPatients(ids).collectLatest {
                for (i in it) {
                    Log.i("!@#pateitns_yeey", i.toString())
                }
            }*/

            /*repo.getDiagnosesWithPatients(2).collectLatest {
                for (pair in it) {
                    val diagnosis = pair.first.toString()
                    val doctor = pair.second.toString()
                    Log.i("!@#disWithDs", "$diagnosis === $doctor")
                }
            }*/
        }
    }

    private fun <T> List<T>.toMyString(): String {
        var result = ""
        for (i in this) {
            result = "$result, $i"
        }
        return "[${result}]"
    }
}