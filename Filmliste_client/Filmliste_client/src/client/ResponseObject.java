package client;

public class ResponseObject {

		private boolean ok;
		private String infoMessage;
		private Film film;
		
		public ResponseObject(boolean ok, String msg, Film f){
			this.setOk(ok);
			setInfoMessage(msg);
			setFilm(f);
		}

		public boolean isOk() {
			return ok;
		}

		public void setOk(boolean ok) {
			this.ok = ok;
		}

		public String getInfoMessage() {
			return infoMessage;
		}

		public void setInfoMessage(String infoMessage) {
			this.infoMessage = infoMessage;
		}

		public Film getFilm() {
			return film;
		}

		public void setFilm(Film film) {
			this.film = film;
		}
}
