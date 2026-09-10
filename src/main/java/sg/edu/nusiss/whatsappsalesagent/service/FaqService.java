package sg.edu.nusiss.whatsappsalesagent.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FaqService {
	
	private final VectorStore vectorStore;
	
//	public FaqService(VectorStore vectorStore) {
//		this.vectorStore = vectorStore;
//	}
	
	
	@PostConstruct
	public void loadFaqs() throws IOException {
		ClassPathResource resource = new ClassPathResource("knowledge/thebubblygem-faq.md");
		
		String content = resource.getContentAsString(StandardCharsets.UTF_8);
		
		String[] sections = content.split("(?=### )");
		
		List<Document> documents = Arrays.stream(sections)
											.filter(section ->
													section.startsWith("### "))
											.map(section -> {
												String cleanedSection = section.replaceAll("(?m)^## (?!#).*$", "").trim();
												
												return new Document(cleanedSection);
											})
//											.map(Document::new)
											.toList();
		
		vectorStore.add(documents);
//		Document faqDocument = new Document(content);
//		vectorStore.add(List.of(faqDocument));
		
		System.out.println("TheBubblyGem FAQ knowledge base loaded!");
		System.out.println("FAQ chunks loaded: " + documents.size());
//		List<Document> faqs = List.of(
//				new Document("""
//		                FAQ CATEGORY: Returns and Exchanges
//
//		                TEST INFORMATION:
//		                Eligible dresses may be returned within 99 days
//		                of receiving the order, provided the item is unworn.
//
//		                The 99-day period is artificial test data only.
//		                """),
//
//		            new Document("""
//		                FAQ CATEGORY: Delivery
//
//		                TEST INFORMATION:
//		                Local delivery costs 77 dollars.
//
//		                The 77-dollar delivery fee is artificial test data only.
//		                """),
//
//		            new Document("""
//		                FAQ CATEGORY: Sizing
//
//		                TEST INFORMATION:
//		                Customers should refer to the product-specific size
//		                guide when choosing a dress size.
//
//		                If the customer is uncertain between sizes, the enquiry
//		                may require additional assistance.
//		                """)
//
//				);
//		
//		vectorStore.add(faqs);
//		System.out.println("FAQ vector store loaded!");
	}
	
	public String findRelevantFaq(String question) {
		List<Document> results = vectorStore.similaritySearch(
				SearchRequest.builder()
					.query(question)
					.topK(3)
					.similarityThreshold(0.58)
					.build());
		
		System.out.println("\n========== RAG DEBUG ==========");
		System.out.println("Customer question:");
		System.out.println(question);

		
		if(results == null || results.isEmpty()) {
			System.out.println("\nNo FAQ results found.");
	        System.out.println("===============================\n");
			return "NO_RELEVANT_INFORMATION_FOUND";
		}
		
//		String retrievedFaq = results.get(0).getText();
		
		
//		System.out.println("\nRetrieved FAQ:");
//		System.out.println(retrievedFaq);
		
		
		System.out.println("\nTop matches:");
		for(int i=0; i<results.size(); i++) {
			Document document = results.get(i);
			System.out.println("\n--- Result " + (i+1) + " ---");
			System.out.println("Score: " + document.getScore());
			System.out.println(document.getText());
		}
		System.out.println("===============================\n");
//		return results.get(0).getText();
		
		StringBuilder context = new StringBuilder();
		
		for(Document document : results) {
			context.append(document.getText());
			context.append("\n\n---\n\n");
		}
		return context.toString();
	}

//	public String findRelevantFaq(String question) {
//		String lowerQuestion = question.toLowerCase();
//		
//		if(lowerQuestion.contains("delivery") || lowerQuestion.contains("shipping")) {
//			return """
//					FAQ CATEGORY: Delivery
//
//					This is placeholder delivery information for our prototype.
//
//	                The assistant must answer using only the information provided here.
//					""";
//		}
//		
//		if(lowerQuestion.contains("return") || lowerQuestion.contains("exchange")) {
////			return """
////			        TEST COMPANY INFORMATION:
////
////			        Customers may return eligible dresses within 99 days
////			        of receiving the order.
////
////			        The item must be unworn.
////
////			        IMPORTANT:
////			        The value "99 days" is artificial test data used only
////			        to verify that the AI uses retrieved information.
////			        """;
//			return """
//					FAQ CATEGORY: Returns and Exchanges
//
//	                This is placeholder returns information for our prototype.
//	
//	                The assistant must answer using only the information provided here.
//					""";
//		}
//		
//		if(lowerQuestion.contains("size") || lowerQuestion.contains("sizing")) {
//			return """
//					FAQ CATEGORY: Sizing
//
//	                Product sizing information should be obtained from
//	                the approved company size guide.
//					""";
//		}
//		
//		return """
//				NO_RELEVANT_INFORMATION_FOUND
//				""";
//	}
}
