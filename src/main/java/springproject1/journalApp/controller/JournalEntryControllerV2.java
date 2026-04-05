package springproject1.journalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import springproject1.journalApp.entity.JournalEntry;
import springproject1.journalApp.entity.User;
import springproject1.journalApp.service.JournalEntryService;
import springproject1.journalApp.service.UserService;

import java.util.*;
@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


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
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // POST /journal
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(
            @RequestBody JournalEntry myEntry,
            @RequestParam String username
    ) {
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
}