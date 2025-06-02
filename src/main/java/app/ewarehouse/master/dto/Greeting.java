package app.ewarehouse.master.dto;

public class Greeting {

    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String content;

    public Greeting() {}

    public Greeting(String content) {
        this.content = content;
    }

   
}
