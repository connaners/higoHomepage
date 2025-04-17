import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import HigoHomePage as higoKeyword
import com.kms.katalon.core.testobject.ConditionType as ConditionType

'Solusi Menyeluruh'
WebUI.verifyElementVisible(findTestObject('Page_higo_dashboard/higo_banner'))

WebUI.verifyElementVisible(findTestObject('Page_higo_dashboard/higo_impact_section'))

'Matrics List'
List<Map> expectedStats = [[('angka') : '80.000.000+', ('label') : 'Impresi bulanan'], [('angka') : '> 70.70%', ('label') : 'Pengguna WiFi aktif bulanan']
    , [('angka') : '47.30%', ('label') : 'Pengguna login kembali'], [('angka') : '136', ('label') : 'Kota dan kawasan terjangkau']
    , [('angka') : '200+', ('label') : 'Kampanye melalui iklan WiFi'], [('angka') : '70', ('label') : 'Kerja sama']]

higoKeyword.verifyImpactMetrics(expectedStats)

WebUI.verifyElementVisible(findTestObject('Page_higo_dashboard/higo_services_section'))

'List Cards Services'
List<Map> expectedCards = [[('judul') : 'WiFi Advertising', ('deskripsi') : 'iklan'], [('judul') : 'HIGOspot', ('deskripsi') : 'pengunjung']
    , [('judul') : 'Integrated Digital Agency', ('deskripsi') : 'strategis']]

higoKeyword.verifyServiceCards(expectedCards)

WebUI.verifyElementVisible(findTestObject('Page_higo_dashboard/higo_reviews_section'))

TestObject activeReview = new TestObject().addProperty('xpath', ConditionType.EQUALS, '//div[@role=\'group\']//article')

String prevText = WebUI.getText(activeReview).trim()

WebUI.click(findTestObject('Page_higo_dashboard/higo_btn_next_slide'))

WebUI.delay(2)

higoKeyword.verifyReviewChanged(prevText)

WebUI.click(findTestObject('Page_higo_dashboard/higo_btn_previous_slide'))

WebUI.delay(2)

higoKeyword.verifyReviewChanged(prevText)

WebUI.verifyElementVisible(findTestObject('Page_higo_dashboard/higo_partners_client_section'))

WebUI.verifyElementVisible(findTestObject('Page_higo_dashboard/higo_faq_section'))

'List faq'
List<Map> expectedFaq = [[('question') : 'Apa itu HIGO?'], [('question') : 'Apa saja layanan HIGO?'], [('question') : 'Bagaimana caranya saya bisa bekerjasama dengan HIGO?']
    , [('question') : 'Apakah saya bisa request target Audience serta lokasi yang saya inginkan?'], [('question') : 'Apakah bisa menggunakan HIGO Tech untuk luar Jawa?']
    , [('question') : 'Apakah HIGO bisa handle kebutuhan Social Media?']]

for (Map faq : expectedFaq) {
    higoKeyword.clickFAQByQuestion(faq)

    WebUI.delay(1)
}

WebUI.mouseOver(findTestObject('Page_higo_services/higo_layanan'))

WebUI.delay(1)

WebUI.click(findTestObject('Page_higo_services/higo_layanan_wifi_ads'))

