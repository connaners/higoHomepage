import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.testobject.ObjectRepository
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import com.kms.katalon.core.testobject.ConditionType


public class HigoHomePage {

	@Keyword
	def static verifyImpactMetrics(List<Map<String, String>> expectedStats) {
		List<WebElement> stats = WebUiCommonHelper.findWebElements(
				ObjectRepository.findTestObject('Page_higo_dashboard/higo_data_metrics'), 10)

		if (stats.size() != expectedStats.size()) {
			KeywordUtil.markFailed("Jumlah item tidak sesuai. Ditemukan: ${stats.size()}, Ekspektasi: ${expectedStats.size()}")
			return
		}

		for (int i = 0; i < stats.size(); i++) {
			WebElement item = stats[i]
			String actualAngka = item.findElement(By.tagName("h6")).getText().trim()
			String actualLabel = item.findElement(By.tagName("p")).getText().trim()
			String expectedAngka = expectedStats[i].angka
			String expectedLabel = expectedStats[i].label

			if (actualAngka != expectedAngka || actualLabel != expectedLabel) {
				KeywordUtil.markFailed("❌ Item ke-${i + 1} tidak valid.\n  Expected: [${expectedAngka} | ${expectedLabel}]\n  Actual:   [${actualAngka} | ${actualLabel}]")
			} else {
				KeywordUtil.logInfo("✅ Item ke-${i + 1} valid: [${actualAngka} | ${actualLabel}]")
			}
		}
	}

	@Keyword
	def static verifyServiceCards(List<Map<String, String>> expectedCards) {
		List<WebElement> cards = WebUiCommonHelper.findWebElements(
				ObjectRepository.findTestObject('Page_higo_dashboard/higo_services_cards'), 10)

		if (cards.size() != expectedCards.size()) {
			KeywordUtil.markFailed("Jumlah card tidak sesuai. Ditemukan ${cards.size()}, ekspektasi ${expectedCards.size()}")
			return
		}

		for (int i = 0; i < cards.size(); i++) {
			Map<String, String> expected = expectedCards[i]
			WebElement card = cards[i]

			def result = extractCardValues(card)
			validateCard(result.title, result.desc, result.btn, expected, i + 1)
		}
	}

	@Keyword
	def static verifyReviewChanged(String prevText) {
		TestObject article = new TestObject().addProperty("xpath", ConditionType.EQUALS,
				"//div[@role='group']//article")

		String currentText = WebUI.getText(article).trim()

		if (prevText == currentText) {
			KeywordUtil.markFailed("❌ Konten review tidak berubah setelah navigasi.")
		} else {
			KeywordUtil.logInfo("✅ Konten review berubah sesuai navigasi.")
		}
	}

	@Keyword
	def static clickFAQByQuestion(Map<String, String> faqItem) {
		String questionText = faqItem.question

		// Buat TestObject secara dinamis
		TestObject faqButton = new TestObject()
		faqButton.addProperty("xpath", ConditionType.EQUALS,
				"//section[contains(@class, 'grid-full-container')]//button[contains(., '" + questionText + "')]")

		if (WebUI.verifyElementPresent(faqButton, 10, FailureHandling.OPTIONAL)) {
			WebUI.click(faqButton)
			KeywordUtil.logInfo("✅ FAQ diklik: '${questionText}'")
		} else {
			KeywordUtil.markFailed("❌ FAQ tidak ditemukan: '${questionText}'")
		}
	}


	private static Map extractCardValues(WebElement card) {
		String title = card.findElement(By.tagName("h6")).getText().trim()
		String desc = card.findElement(By.xpath(".//p")).getAttribute("innerText").trim()
		String btn = card.findElement(By.xpath(".//button//span")).getAttribute("innerText").trim()
		return [title: title, desc: desc, btn: btn]
	}

	private static void validateCard(String title, String desc, String btnText, Map expected, int index) {
		String expectedTitle = expected.judul
		String expectedDesc = expected.deskripsi
		String expectedBtn = "Pelajari lebih lanjut"

		String errorDetail = ""

		if (title != expectedTitle) {
			errorDetail += "\n  - Judul tidak cocok. Expected: '${expectedTitle}' | Actual: '${title}'"
		}
		if (!desc.contains(expectedDesc)) {
			errorDetail += "\n  - Deskripsi tidak mengandung. Expected (partial): '${expectedDesc}' | Actual: '${desc}'"
		}
		if (btnText != expectedBtn) {
			errorDetail += "\n  - Teks tombol tidak cocok. Expected: '${expectedBtn}' | Actual: '${btnText}'"
		}

		if (errorDetail) {
			KeywordUtil.markFailed("❌ Card ke-${index} tidak valid:${errorDetail}")
		} else {
			KeywordUtil.logInfo("✅ Card ke-${index} valid: [${title}] [${desc}] [${btnText}]")
		}
	}
}
