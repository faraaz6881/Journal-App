package springproject1.journalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import springproject1.journalApp.entity.JournalEntry;
import springproject1.journalApp.entity.User;
import springproject1.journalApp.service.GeminiService;
import springproject1.journalApp.service.JournalEntryService;
import springproject1.journalApp.service.UserService;

import java.util.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @Autowired
    private GeminiService geminiService;

    // GET /journal/{username}
    @GetMapping("/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        System.out.println("user exists but no journals");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // POST /journal
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(
            @RequestBody JournalEntry myEntry,
            @RequestParam String username
    ) {
        System.out.println(username);
        System.out.println(myEntry);
        try {
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // GET /journal/id/{id}
    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable Long id) {

        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);

        return journalEntry
                .map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT /journal/id/{id}
    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable Long id,
            @RequestBody JournalEntry newEntry
    ) {

        JournalEntry old = journalEntryService.findById(id).orElse(null);

        if (old != null) {
            old.setTitle(
                    newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()
                            ? newEntry.getTitle()
                            : old.getTitle()
            );

            old.setContent(
                    newEntry.getContent() != null && !newEntry.getContent().isEmpty()
                            ? newEntry.getContent()
                            : old.getContent()
            );

            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(
            @PathVariable Long id,
            @RequestParam String username
    ) {

        try {
            journalEntryService.DeleteById(id, username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/grammar-check")
    public ResponseEntity<String> checkGrammar(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.isBlank()) {
            return new ResponseEntity<>("Text cannot be empty.", HttpStatus.BAD_REQUEST);
        }
        String result = geminiService.checkGrammar(text);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/id/{id}/grammar-check")
    public ResponseEntity<String> checkGrammarOfEntry(@PathVariable Long id) {
        JournalEntry entry = journalEntryService.findById(id).orElse(null);
        if (entry == null) {
            return new ResponseEntity<>("Journal entry not found.", HttpStatus.NOT_FOUND);
        }
        if (entry.getContent() == null || entry.getContent().isBlank()) {
            return new ResponseEntity<>("This journal entry has no content to check.", HttpStatus.BAD_REQUEST);
        }
        String result = geminiService.checkGrammar(entry.getContent());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}