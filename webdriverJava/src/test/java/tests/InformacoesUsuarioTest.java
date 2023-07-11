package tests;

import static org.junit.Assert.*;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import suporte.Generator;
import suporte.Screenshot;
import suporte.Web;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "InformacoesUsuarioTestData.csv")
public class InformacoesUsuarioTest {
    private WebDriver navegador;

    @Rule
    public TestName test = new TestName();

    @Before
    public void setUp() {
        navegador = Web.createChrome();

        //clicar no link que possui o texto sign-in
        WebElement linkSignIn = navegador.findElement(By.linkText("Sign in"));
        linkSignIn.click();
        //identificando o campo de login
        WebElement formSignInBox = navegador.findElement(By.id("signinbox"));

        //digitar no campo "login" o usuario: julio0001
        formSignInBox.findElement(By.name("login")).sendKeys("julio0002");

        //digitar no campo "password" a senha: 123456
        formSignInBox.findElement(By.name("password")).sendKeys("654321");

        //clicar no link com o texto "SIGN IN"
        navegador.findElement(By.linkText("SIGN IN")).click();

        //clicar em um link que tem a class me
        navegador.findElement(By.className("me")).click();

        //Clicar aba more data about you (clicar no link que possui o texto "MORE DATA ABOUT YOU")
        navegador.findElement(By.linkText("MORE DATA ABOUT YOU")).click();
    }


    @Test
    public void testAdicionarUmaInformacaoAdicionalDoUsuario(@Param(name="tipo")String tipo, @Param(name="contato")String contato, @Param(name="mensagem")String mensagemEsperada) {
        //clicar no botao add more data about you
        //clicar no botao atraves do seu xpath //button[@data-target="addmoredata"]
        navegador.findElement(By.xpath("//button[@data-target=\"addmoredata\"]")).click();

        //identificar a pop onde esta o formulario de id addmoredata
        WebElement popupAddMoreData = navegador.findElement(By.id("addmoredata"));

        //na combo de name "type" escolher a opcao "Phone"
        WebElement campoType = popupAddMoreData.findElement(By.name("type"));
        new Select(campoType).selectByVisibleText(tipo);

        //no campo de name "contact" digitar "+5511999992222"
        popupAddMoreData.findElement(By.name("contact")).sendKeys(contato);

        //Clicar no link de text "SAVE" que esta na popup
        popupAddMoreData.findElement(By.linkText("SAVE")).click();

        //Na msg de id "toast-container" validar que o texto eh: "Your contact has been added!"
        WebElement mensagemPopUp = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagemPopUp.getText();
        assertEquals(mensagemEsperada, mensagem);
    }

    //@Test
    public void removerUmContatodeUmUsuario() {
        // Clicar no elemento pelo seu xpath //span[text()="+551133334444"]/following-sibling::a
        navegador.findElement(By.xpath("//span[text()=\"+551133334444\"]/following-sibling::a")).click();

        //Confimar a janela javascript
        navegador.switchTo().alert().accept();

        // Validar que a mensagem apresentada foi Rest in peace, dear phone!
        WebElement mensagemPopUp = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagemPopUp.getText();
        assertEquals("Rest in peace, dear phone!", mensagem);

        String screenshotArquivo = "C:\\Users\\julie\\testReport\\Taskit\\"+ Generator.dataHoraParaArquivo() + test.getMethodName()+ ".png";
        Screenshot.tirar(navegador, screenshotArquivo);

        //Aguardar ate 10 segundos para que pop up desapareca
        WebDriverWait aguardar = new WebDriverWait(navegador, Duration.ofSeconds(10));
        aguardar.until(ExpectedConditions.stalenessOf(mensagemPopUp));

        //Clicar no link com texto "logout"
        navegador.findElement(By.linkText("Logout")).click();
    }

    @After
    public void tearDown() {
        //fechar o navegador
        //navegador.quit();
    }
}
