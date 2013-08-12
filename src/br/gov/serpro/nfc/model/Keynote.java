package br.gov.serpro.nfc.model;

import java.util.List;

import br.gov.serpro.nfc.service.KeynoteService;
import br.gov.serpro.nfc.service.ServiceFactory;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

/**
 * Model representando uma Palestra.
 * 
 * @author Marlon Silva Carvalho
 */
@Table(name = "Keynotes")
public class Keynote extends Model {

	@Column(name = "service_id")
	public Long service_id;
	
	@Column(name = "description")
	public String description;

	@Column(name = "date")
	public String date;

	@Column(name = "speaker")
	public String speaker;

	/**
	 * Obter a Lista de todas as palestras disponíveis.
	 * 
	 * @return Lista de Palestras.
	 */
	public static List<Keynote> refresh() {
		KeynoteService service = ServiceFactory.getKeynoteService();
		List<Keynote> keynotes = service.list(1L);

		ActiveAndroid.beginTransaction();
		new Delete().from(Keynote.class).execute();

		try {
			for (Keynote keynote : keynotes) {
				keynote.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}

		return keynotes;
	}

	/**
	 * Obter a Lista de todas as palestras disponíveis.
	 * 
	 * @return Lista de Palestras.
	 */
	public static List<Keynote> all() {
		return new Select().from(Keynote.class).execute();
	}

	/**
	 * Realizar o checkin de um usuário nesta palestra.
	 * 
	 * @param user
	 *            Usuário que fará o checkin.
	 */
	public void checkin(User user) {
		ServiceFactory.getKeynoteService().checkin(this, user);
	}

	/**
	 * Realizar o checkout de um usuário nesta palestra.
	 * 
	 * @param user
	 *            Usuário que fará o checkout.
	 */
	public void checkout(User user) {
		ServiceFactory.getKeynoteService().checkout(this, user);
	}

	/**
	 * Obter uma Palestra pelo seu Identificador.
	 * 
	 * @param id
	 *            Identificador.
	 * @return
	 */
	public static Keynote byId(Long id) {
		return new Select().from(Keynote.class).where("service_id=?", id).executeSingle();
	}

	/**
	 * Obter todas as palestras que estão acontecendo agora.
	 * 
	 * @return Lista de Palestras.
	 */
	public static List<Keynote> takingPlaceNow() {
		return all();
	}

}
