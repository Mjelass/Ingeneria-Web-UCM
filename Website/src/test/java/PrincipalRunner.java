
import com.intuit.karate.junit5.Karate;


class PrincipalRunner {

	@Karate.Test
    Karate testPrincipal() {
        return Karate.run("principal").relativeTo(getClass());
    }

}
