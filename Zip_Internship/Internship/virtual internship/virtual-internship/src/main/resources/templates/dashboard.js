document.addEventListener('DOMContentLoaded', function() {
    const taskDomainSelect = document.getElementById('taskDomainSelect');
    const taskInternshipSelect = document.getElementById('taskInternshipSelect');
    const addDomainForm = document.getElementById('addDomainForm');
    const domainNameInput = document.getElementById('domainNameInput');
    const domainDescriptionInput = document.getElementById('domainDescriptionInput');
    const domainsListContainer = document.getElementById('domainsList');
    const internshipDomainSelect = document.getElementById('internshipDomainSelect');

    // Load internships for selected domain in task form
    if (taskDomainSelect && taskInternshipSelect) {
        taskDomainSelect.addEventListener('change', function() {
            const selectedDomain = this.value;
            // Clear internship options
            taskInternshipSelect.innerHTML = '<option value="" disabled selected>Select Internship</option>';

            if (selectedDomain) {
                fetch('/api/domains/byName/' + encodeURIComponent(selectedDomain))
                    .then(response => response.json())
                    .then(domain => {
                        if (domain && domain.internships) {
                            domain.internships.forEach(internship => {
                                const option = document.createElement('option');
                                option.value = internship.name || internship.title || internship.id;
                                option.textContent = internship.name || internship.title;
                                taskInternshipSelect.appendChild(option);
                            });
                        }
                    })
                    .catch(error => console.error('Error loading internships:', error));
            }
        });
    }

    // Handle add domain form submission via AJAX
    if (addDomainForm) {
        addDomainForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const name = domainNameInput.value.trim();
            const description = domainDescriptionInput.value.trim();

            if (!name) {
                alert('Please enter a domain name');
                return;
            }
            if (!description) {
                alert('Please enter a domain description');
                return;
            }

            fetch('/api/domains', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    name: name,
                    description: description
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to add domain');
                }
                return response.json();
            })
            .then(newDomain => {
                // Clear form inputs
                domainNameInput.value = '';
                domainDescriptionInput.value = '';

                // Update domains list in the dashboard
                updateDomainsList(newDomain);

                // Update domain dropdowns
                updateDomainDropdowns(newDomain);
            })
            .catch(error => {
                console.error('Error adding domain:', error);
                alert('Error adding domain: ' + error.message);
            });
        });
    }

    function updateDomainsList(newDomain) {
        if (!domainsListContainer) return;

        // Remove "No domains added yet." message if present
        const noDomainsMsg = domainsListContainer.querySelector('p');
        if (noDomainsMsg && noDomainsMsg.textContent.includes('No domains added yet')) {
            noDomainsMsg.remove();
        }

        // Create new domain accordion element
        const domainAccordion = document.createElement('div');
        domainAccordion.classList.add('accordion');

        const header = document.createElement('div');
        header.classList.add('accordion-header');
        header.innerHTML = `<span>${newDomain.name}</span><i class="fas fa-chevron-down"></i>`;
        header.addEventListener('click', function() {
            this.parentElement.classList.toggle('accordion-active');
        });

        const content = document.createElement('div');
        content.classList.add('accordion-content');
        content.innerHTML = `<p>No internships in this domain yet.</p>`;

        domainAccordion.appendChild(header);
        domainAccordion.appendChild(content);

        domainsListContainer.appendChild(domainAccordion);
    }

    function updateDomainDropdowns(newDomain) {
        if (internshipDomainSelect) {
            const option = document.createElement('option');
            option.value = newDomain.name;
            option.textContent = newDomain.name;
            internshipDomainSelect.appendChild(option);
        }
        if (taskDomainSelect) {
            const option = document.createElement('option');
            option.value = newDomain.name;
            option.textContent = newDomain.name;
            taskDomainSelect.appendChild(option);
        }
    }
});
