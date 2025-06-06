import com.alex.domain.Note
import com.alex.repository.NoteEntity

object Fixtures {
    object Notes {
        object Domain {
            val dinner = Note(10, "Make Dinner", "Healthy", 17234824, 17234824)
        }
        object Entity {
            val dinner = NoteEntity(10, "Make Dinner", "Healthy", 17234824, 17234824)
        }
    }
}