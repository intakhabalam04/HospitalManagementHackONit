// blood-donor-list.js



window.onload = function() {
  fetch('/admin/blood-donations-list') // change this to your actual API endpoint
    .then(response => response.json())
    .then(data => populateTable(data, 'bloodDonationTable'));
}

function populateTable(data, tableId) {
  const table = document.getElementById(tableId);
  const headers = Array.from(table.querySelectorAll('th')).map(th => th.textContent.toLowerCase().replace(/\s/g, ''));

  const headerToPropertyMap = {
    'dateofbirth': 'dateOfBirth',
    'bloodgroup': 'bloodGroup',
    // Add more mappings here if needed
  };

  data.forEach(item => {
    const row = table.insertRow();
    headers.forEach(header => {
      const property = headerToPropertyMap[header] || header;
      const cell = row.insertCell();
      cell.textContent = item[property] || '';
    });
  });
}


;