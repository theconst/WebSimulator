/* 
 * simple script to list students, upload files and perform other admin actions
 */
$(window).on('load', function () {
    
    /* propagate click to file upload form */
    $('#fileupload-visible').click(function() {
        $('#fileupload-hidden').trigger('click');
    });
    
    /* upload file as multipart / form data to server */
    $('#upload').click(function() {
        $.ajax({
            url: '/WebSimulator/uploadmodel',
            method: 'POST',
            data: $('#uploadbanner').serialize(),
            contentType: 'multipart/form-data',
            async: true,
            success: function(data) {
                console.log(data);
            }
        });  
    });
    
    /* list students */
    $('#list').click(function () {
        getListOfStutends(function (studentList) {
            /* clear table */
            $('#student-list-content').replaceWith(
                    toTable(studentList, ['login', 'name', 'group', 'variant']));
        }, function (_, status, _) {
            console.log('Response status: ' + status);
        });
    });
    
    /**
     * Gets list of students
     * @param {success} callback called on success
     * @param {failure} callback called on failure
     */
    function getListOfStutends(success, failure) {
       $.ajax({
            url: '/WebSimulator/liststudents',
            method: 'GET',
            async: true,
            cache: false,
            dataType: 'json',
            error: failure,
            success: success
        });
    }

    /**
     * Creates table from object
     * 
     * @param {object} obj object 
     * @param {array} props properties included in the table
     * @returns {string} contents of object in table form
     */
    function toTable(obj, props) {

        /* Simple helper functions */
        function entry(e) {
            //set value as text area
            return '<td><input type="text" value="' + e + '"/></td>';
        }

        function row(r) {
            return '<tr>' + r + '</tr>';
        }

        /* map to html table */
        return obj.map(function (field) {
            return row(props.map(function (prop) {
                return entry(field[prop]);
            }));
        }).join();
    }
});


